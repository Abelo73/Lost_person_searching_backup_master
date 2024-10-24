package com.act.Gakos.service;

import com.act.Gakos.entity.Image;
import com.act.Gakos.entity.ProfilePicture;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.ImageRepository;
import com.act.Gakos.repository.ProfilePictureRepository;
import com.act.Gakos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfilePictureService {

    private final ProfilePictureRepository profilePictureRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    private static final String STORAGE_DIRECTORY = "/home/abel/eclipse-workspace/Desktop/lostImages";
    private static final String PROFILE_STORAGE_DIRECTORY = STORAGE_DIRECTORY + "/profileStorage";

    private final static Logger logger = LoggerFactory.getLogger(ProfilePictureService.class);

    @Autowired
    public ProfilePictureService(ProfilePictureRepository profilePictureRepository,
                                 ImageRepository imageRepository,
                                 UserRepository userRepository) {
        this.profilePictureRepository = profilePictureRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProfilePicture saveProfilePicture(Integer userId, MultipartFile file, String descriptions, String location, LocalDateTime dateOfPictureTaken) throws IOException {
        User user = getUserById(userId);
        logger.debug("Uploading profile picture for user ID: {}", userId);

        validateFileType(file);
        Path filePath = prepareFilePath(file);

        // Save the file
        saveFile(file, filePath);

        // Handle ProfilePicture
        ProfilePicture profilePicture = getOrCreateProfilePicture(user);

        // Create and save Image entity
        Image image = createImageEntity(file, descriptions, location, filePath.getFileName().toString(), profilePicture, filePath,dateOfPictureTaken);
        imageRepository.save(image);

        updateProfilePictureWithImage(profilePicture, image);
        logger.info("Profile picture saved for user ID: {}", userId);

        return profilePicture;
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void validateFileType(MultipartFile file) {
        if (!file.getContentType().startsWith("image/")) {
            throw new InvalidFileTypeException("Invalid file type. Only images are allowed.");
        }
    }

    private Path prepareFilePath(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(PROFILE_STORAGE_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Created upload directory: {}", uploadPath);
        }

        String filename = generateUniqueFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(filename);
        checkFileExistence(filePath);
        return filePath;
    }

    private void saveFile(MultipartFile file, Path filePath) throws IOException {
        Files.copy(file.getInputStream(), filePath);
        logger.info("File uploaded: {}", filePath.getFileName());
    }

    private String generateUniqueFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    private void checkFileExistence(Path filePath) throws FileAlreadyExistsException {
        if (Files.exists(filePath)) {
            throw new FileAlreadyExistsException("File with the same name already exists");
        }
    }

    private ProfilePicture getOrCreateProfilePicture(User user) {
        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture == null) {
            profilePicture = new ProfilePicture();
            profilePicture.setUser(user);
            profilePicture.setImages(new ArrayList<>()); // Initialize the list of images
            profilePicture = profilePictureRepository.save(profilePicture);
            logger.info("Profile picture entity created and saved for user: {}", user.getId());
        }
        return profilePicture;
    }

    private Image createImageEntity(MultipartFile file, String descriptions, String location, String filename, ProfilePicture profilePicture, Path filePath, LocalDateTime dateOfPictureTaken) throws IOException {
        Image image = new Image();
        image.setImageName(filename);
        image.setImagePath(filePath.toString()); // Save the file path
        image.setRegisteredDate(LocalDateTime.now());
        image.setDescription(descriptions);
//        image.setDateOfPictureTaken(LocalDateTime.now());
        image.setDateOfPictureTaken(dateOfPictureTaken);
        image.setLocationOfPictureTaken(location);
        image.setSize(file.getSize());
        image.setType(file.getContentType());
        image.setProfilePicture(profilePicture);
        image.setImageData(file.getBytes());
        return image;
    }

    private void updateProfilePictureWithImage(ProfilePicture profilePicture, Image image) {
        List<Image> images = profilePicture.getImages();
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
        profilePicture.setImages(images);
        profilePictureRepository.save(profilePicture);
        logger.info("Image added to profile picture: {}", image.getImageName());
    }

    @Transactional
    public ProfilePicture updateProfilePicture(Integer userId, Integer profilePictureId, MultipartFile file, String description, String location, LocalDateTime dateOfPictureTaken) throws IOException {
        // Find the existing profile picture by its ID
        ProfilePicture existingProfilePicture = profilePictureRepository.findById(profilePictureId)
                .orElseThrow(() -> new RuntimeException("Profile picture not found"));

        // Validate user ownership
        if (!existingProfilePicture.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this profile picture");
        }

        // Update fields
        if (file != null && !file.isEmpty()) {
            // Handle file storage logic here (update the image)
            // Ensure an image exists before attempting to delete it
            if (!existingProfilePicture.getImages().isEmpty()) {
                Path oldFilePath = Paths.get(existingProfilePicture.getImages().get(0).getImagePath());
                Files.deleteIfExists(oldFilePath); // Delete old file if needed
            }

            // Prepare and save the new file
            Path filePath = prepareFilePath(file);
            saveFile(file, filePath);

            // Create a new Image entity
            Image newImage = createImageEntity(file, description, location, filePath.getFileName().toString(), existingProfilePicture, filePath, dateOfPictureTaken);
            imageRepository.save(newImage);

            // Update the profile picture with the new image
            updateProfilePictureWithImage(existingProfilePicture, newImage);
        }

        // Update description in the existing image if necessary (optional)
        if (!existingProfilePicture.getImages().isEmpty()) {
            Image existingImage = existingProfilePicture.getImages().get(0); // Assuming only one image for profile picture
            existingImage.setDescription(description);
            existingImage.setLocationOfPictureTaken(location);
            existingImage.setDateOfPictureTaken(dateOfPictureTaken);
            imageRepository.save(existingImage); // Save the updated image
        }

        // Save the updated profile picture
        return profilePictureRepository.save(existingProfilePicture);
    }


    @Transactional
    public void deleteProfilePicture(Integer userId, Integer profilePictureId) throws IOException {
        ProfilePicture profilePicture = profilePictureRepository.findById(profilePictureId)
                .orElseThrow(() -> new RuntimeException("Profile picture not found"));

        // Validate that the profile picture belongs to the user
        if (!profilePicture.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this profile picture");
        }

        // Check if there are images associated with the profile picture
        List<Image> images = profilePicture.getImages();
        if (images != null && !images.isEmpty()) {
            for (Image image : images) {
                // Delete the image file from the file system
                Path filePath = Paths.get(image.getImagePath());
                Files.deleteIfExists(filePath);

                // Delete the image from the database
                imageRepository.delete(image);
                logger.info("Deleted image: {}", image.getImageName());
            }
        }

        // Delete the profile picture entity from the database
        profilePictureRepository.delete(profilePicture);
        logger.info("Deleted profile picture for user ID: {}", userId);
    }





    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidFileTypeException extends RuntimeException {
        public InvalidFileTypeException(String message) {
            super(message);
        }
    }



//    public List<ProfilePicture> getProfilePicturesByUserId(Integer userId){
//        return profilePictureRepository.findByUserId(userId);
//    }

    public Page<ProfilePicture> getProfilePicturesByUserId(Integer userId, Pageable pageable) {
        return profilePictureRepository.findByUserId(userId, pageable); // Use pageable here.
    }
}
