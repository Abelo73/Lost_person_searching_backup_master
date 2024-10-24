package com.act.Gakos.service;

import com.act.Gakos.entity.*;
import com.act.Gakos.exceptions.ResourceNotFoundException;
import com.act.Gakos.repository.ImageMetadataRepository;
import com.act.Gakos.repository.UserRepository;
import com.act.Gakos.response.BaseResponse;
import com.act.Gakos.response.ImageBaseResponse;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String STORAGE_DIRECTORY = "/home/abel/eclipse-workspace/Desktop/lostImages";
    private static final String PROFILE_STORAGE_DIRECTORY = STORAGE_DIRECTORY + "/profileStorage";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageMetadataRepository imageMetadataRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method to delete a user if they have the role of SUPER_ADMIN.
     *
     * @param id The ID of the user to be deleted.
     * @return ResponseEntity with appropriate message.
     */
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> deleteUser(Integer id) {
        logger.info("Attempting to delete user with ID: {}", id);

        // Fetch the user by ID
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User found: {}", user);

            // Check if the user's role is different from SUPER_ADMIN
            if (user.getRole() != Role.SUPER_ADMIN) {
                userRepository.deleteById(id);
                var role = user.getRole();
                logger.info("User with ID: {} and role {} has been deleted.", id, role);
                return ResponseEntity.ok("User with"+ user + " role has been deleted.");
            } else {
                logger.warn("Attempted to delete user with ID: {} but role is not SUPER_ADMIN. User role: {}", id, user.getRole());
                return ResponseEntity.status(403).body("Only users with SUPER_ADMIN role can be deleted.");
            }
        } else {
            logger.warn("User with ID: {} not found.", id);
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    // Inside UserService constructor or an @PostConstruct method
    @PostConstruct
    public void init() {
        try {
            // Create the profile storage directory if it doesn't exist
            Files.createDirectories(Paths.get(PROFILE_STORAGE_DIRECTORY));
            logger.info("Profile storage directory created at: {}", PROFILE_STORAGE_DIRECTORY);
        } catch (IOException e) {
            logger.error("Failed to create profile storage directory: {}", e.getMessage());
        }
    }



    public BaseResponse<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                logger.info("No users found");
                return new BaseResponse<>("No users found.", true, null);
            }
            logger.info("Users retrieved successfully");
            return new BaseResponse<>("Users retrieved successfully", true, users);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving users: {}", e.getMessage());
            return new BaseResponse<>("An error occurred while retrieving users", false, null);
        }
    }

    public BaseResponse<User> getUserById(Integer id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
            logger.info("User with id {} retrieved successfully", id);
            return new BaseResponse<>("User retrieved successfully", true, user);
        } catch (ResourceNotFoundException e) {
            logger.warn("Resource not found: {}", e.getMessage());
            return new BaseResponse<>(e.getMessage(), false, null);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving user by id: {}", e.getMessage());
            return new BaseResponse<>("An error occurred while retrieving the user", false, null);
        }
    }

    public BaseResponse<User> createUser(User user) {
        try {
            User createdUser = userRepository.save(user);
            logger.info("User created successfully: {}", createdUser.getUsername());
            return new BaseResponse<>("User created successfully", true, createdUser);
        } catch (Exception e) {
            logger.error("Error occurred while creating user: {}", e.getMessage());
            return new BaseResponse<>("An error occurred while creating the user", false, null);
        }
    }

    public BaseResponse<User> updateUser(Integer id, User userDetails) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

            // Update user fields
            user.setFirstName(userDetails.getFirstName());
            user.setMiddleName(userDetails.getMiddleName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
//            user.setUsername(userDetails.getUsername());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setPassword(userDetails.getPassword());

            User updatedUser = userRepository.save(user);
            logger.info("User with id {} updated successfully", id);
            return new BaseResponse<>("User updated successfully", true, updatedUser);
        } catch (ResourceNotFoundException e) {
            logger.warn("Resource not found: {}", e.getMessage());
            return new BaseResponse<>(e.getMessage(), false, null);
        } catch (Exception e) {
            logger.error("Error occurred while updating user: {}", e.getMessage());
            return new BaseResponse<>("An error occurred while updating the user", false, null);
        }
    }




    public BaseResponse<String> resetPassword(String token, String newPassword) {
        User user = userRepository.findByToken(token).orElse(null);
        if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return new BaseResponse<>("Invalid or expired token", false, null);
        }

        user.setPassword(newPassword);  // Ensure to hash the password
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);

        return new BaseResponse<>("Password reset successfully", true, null);
    }




    @Transactional
    public BaseResponse<String> requestPasswordReset(String username) {
        logger.info("Searching for user with username: {}", username);

        // Fetch the user and check for case sensitivity
        var user = userRepository.loadByUsername(username).orElse(null);

        if (user == null) {
            logger.info("User not found with username: {}", username);
            return new BaseResponse<>("User not found", false, null);
        } else {
            var email = user.getEmail();
            logger.info("User found with email: {}", email);

            // Generate a reset token
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1)); // 1-hour validity

            // Save the user
            userRepository.save(user);

            // Send the email
            sendPasswordResetEmail(user.getEmail(), token);
            return new BaseResponse<>("Password reset email sent", true, null);
        }
    }


    private void sendPasswordResetEmail(String email, String token) {
        String resetLink = "http://localhost:8080/api/users/reset-password/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link: " + resetLink);
        logger.info("Message: {}",message);
        mailSender.send(message);
    }


    public void saveFiles(List<MultipartFile> filesToSave, Integer userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ProfileInfo profileInfo = user.getProfileInfo();
        if (profileInfo == null) {
            profileInfo = new ProfileInfo();
            user.setProfileInfo(profileInfo);
        }

        if (filesToSave == null || filesToSave.isEmpty()) {
            logger.info("No files to upload");
            throw new NullPointerException("No files to upload");
        }

        // Initialize the personal images list if it's null
        if (profileInfo.getPersonalImages() == null) {
            profileInfo.setPersonalImages(new ArrayList<>());
        }

        List<ImageMetadataNew> savedFileMetadataList = profileInfo.getPersonalImages();  // Get the current list of metadata

        for (MultipartFile fileToSave : filesToSave) {
            if (fileToSave.isEmpty()) {
                logger.info("Skipping empty file: {}", fileToSave.getOriginalFilename());
                continue;  // Skip empty files
            }

            var targetFile = new File(STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());

            // Security check for unsupported file names
            if (!targetFile.getCanonicalPath().startsWith(new File(STORAGE_DIRECTORY).getCanonicalPath())) {
                logger.info("Unsupported file name");
                throw new SecurityException("Unsupported file name");
            }

            // Save the file to the target directory
            Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Create an ImageMetadata object to store file information
            ImageMetadataNew imageMetadata = new ImageMetadataNew();
            imageMetadata.setFileName(fileToSave.getOriginalFilename());
            imageMetadata.setFileSize(fileToSave.getSize());
            imageMetadata.setContentType(fileToSave.getContentType());
            imageMetadata.setFilePath(targetFile.getAbsolutePath());  // Store the path of the file
            // Add the image metadata to the list
            savedFileMetadataList.add(imageMetadata);
            logger.info("Saved File Path: {}", targetFile.getAbsolutePath());
        }

        // Update the profile info with the new list of saved file metadata
        profileInfo.setPersonalImages(savedFileMetadataList);

        // Save the user along with the updated profile info
        logger.info("Saved in user");
        userRepository.save(user);
    }





    // Convert the byte array to Base64 String for the frontend
    public String convertToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }


public byte[] fetchImageAsByteArray(ImageMetadataNew metadata) {
    try {
        // Create a Path object for the file
        Path path = Path.of(metadata.getFilePath());

        // Read the file content into a byte array
        return Files.readAllBytes(path);
    } catch (IOException e) {
        // Handle the exception
        throw new RuntimeException("Error reading image file: " + metadata.getFileName(), e);
    }
}

    public List<ImageMetadataNew> getPersonalImagesByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ProfileInfo profileInfo = user.getProfileInfo();
        if (profileInfo == null || profileInfo.getPersonalImages() == null) {
            throw new IllegalArgumentException("No images found for the user");
        }

        List<ImageMetadataNew> imageMetadataList = new ArrayList<>();
        for (ImageMetadataNew metadata : profileInfo.getPersonalImages()) {
            byte[] imageBytes = fetchImageAsByteArray(metadata); // Implement this method
            metadata.setBase64Image(convertToBase64(imageBytes)); // Set Base64 image
            imageMetadataList.add(metadata);
        }

        return imageMetadataList;
    }


    @Transactional
    public void updateLastSeen(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ProfilePictureService.UserNotFoundException("User not found with ID: " + userId);
        }
        userRepository.updateLastSeen(userId, LocalDateTime.now());
    }


//    public List<ImageMetadataNew> saveProfileImages(Integer userId, List<MultipartFile> files) throws IOException {
//        List<ImageMetadataNew> imageMetadataList = new ArrayList<>();
//        for (MultipartFile file : files) {
//            // The same logic as in saveProfileImage...
//            if (file.isEmpty()) {
//                logger.info("File is empty");
//                throw new IllegalArgumentException("File is empty");
//            }
//
//            Path filePath = Paths.get(PROFILE_STORAGE_DIRECTORY, file.getOriginalFilename());
//            if (Files.exists(filePath)) {
//                String message = "Filename already exists: " + file.getOriginalFilename();
//                logger.warn(message);
//                throw new IllegalArgumentException(message);
//            }
//
//            try {
//                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                logger.info("Profile picture uploaded successfully: {}", file.getOriginalFilename());
//            } catch (IOException e) {
//                logger.error("Error occurred while uploading profile picture: {}", e.getMessage());
//                throw new IOException("Error occurred while uploading profile picture", e);
//            }
//
//            // Create ImageMetadataNew object
//            ImageMetadataNew imageMetadata = new ImageMetadataNew();
//            imageMetadata.setFileName(file.getOriginalFilename());
//            imageMetadata.setFileSize(file.getSize());
//            imageMetadata.setContentType(file.getContentType());
//            imageMetadata.setFilePath(filePath.toString());
//
//            // Associate with user
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
//            ProfileInfo profileInfo = user.getProfileInfo();
//
//            if (profileInfo == null) {
//                profileInfo = new ProfileInfo();
//                user.setProfileInfo(profileInfo);
//            }
//
//            // Update the profile image metadata
//            profileInfo.setProfilePicture((List<ImageMetadataNew>) imageMetadata);
//            userRepository.save(user); // Save user
//
//            imageMetadataList.add(imageMetadata); // Add to list
//        }
//        return imageMetadataList; // Return the list of uploaded images' metadata
//    }


    public ImageMetadataNew saveProfileImage(Integer userId, MultipartFile file) throws IOException {
        // Validate and save the file
        if (file.isEmpty()) {
            logger.info("File is empty");
            throw new IllegalArgumentException("File is empty");
        }

        // Define the path where the file will be stored
        Path filePath = Paths.get(PROFILE_STORAGE_DIRECTORY, file.getOriginalFilename());

        // Check if the file already exists
        if (Files.exists(filePath)) {
            String message = "Filename already exists: " + file.getOriginalFilename();
            logger.warn(message);
            throw new IllegalArgumentException(message); // Throw exception or return a message as per your design
        }

        // Try to copy the file to the defined path
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Profile picture uploaded successfully: {}", file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("Error occurred while uploading profile picture: {}", e.getMessage());
            throw new IOException("Error occurred while uploading profile picture", e);
        }

        // Create and return ImageMetadataNew object
        ImageMetadataNew imageMetadata = new ImageMetadataNew();
        imageMetadata.setFileName(file.getOriginalFilename());
        imageMetadata.setFileSize(file.getSize());
        imageMetadata.setContentType(file.getContentType());
        imageMetadata.setFilePath(filePath.toString()); // Store the path of the file

        // Optionally, update the user's profile info with the new profile image metadata
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ProfileInfo profileInfo = user.getProfileInfo();

        if (profileInfo == null) {
            profileInfo = new ProfileInfo();
            user.setProfileInfo(profileInfo);
        }

        // Update the profile image metadata
        profileInfo.setProfilePicture((List<ImageMetadataNew>) imageMetadata);
        userRepository.save(user); // Save the user to persist changes

        return imageMetadata;
    }


//    public ImageMetadataNew saveProfileImage(Integer userId, MultipartFile file) throws IOException {
//        // Validate and save the file
//        if (file.isEmpty()) {
//            logger.info("File is empty");
//            throw new IllegalArgumentException("File is empty");
//        }
//
//        // Define the path where the file will be stored
//        Path filePath = Paths.get(PROFILE_STORAGE_DIRECTORY, file.getOriginalFilename());
//
//        // Check if the file already exists
//        if (Files.exists(filePath)) {
//            String message = "Filename already exists: " + file.getOriginalFilename();
//            logger.warn(message);
//            throw new IllegalArgumentException(message); // Throw exception or return a message as per your design
//        }
//
//        // Try to copy the file to the defined path
//        try {
//            Files.copy(file.getInputStream(), filePath);
//        } catch (IOException e) {
//            logger.error("IOException occurred while saving file: " + e.getMessage(), e);
//            throw new RuntimeException("Error saving the file", e);
//        }
//
//        // Create and save the ImageMetadataNew object
//        ImageMetadataNew imageMetadata = new ImageMetadataNew();
//        imageMetadata.setUserId(userId);
//        imageMetadata.setFileName(file.getOriginalFilename());
//        imageMetadata.setUploadDate(LocalDateTime.now());
//        imageMetadata.setDescriptions(file.getResource().getDescription());
//
//        // Save imageMetadata to your database
//         imageMetadataRepository.save(imageMetadata);
//        return imageMetadata;
//    }

//    public ImageMetadataNew saveProfileImage(Integer userId, MultipartFile file) throws IOException {
//        // Validate and save the file
//        if (file.isEmpty()) {
//            logger.info("File is empty");
//            throw new IllegalArgumentException("File is empty");
//        }
//
//        // Define the path where the file will be stored
//        Path filePath = Paths.get(STORAGE_DIRECTORY, file.getOriginalFilename());
//
//        // Check if the file already exists
//        if (Files.exists(filePath)) {
//            String message = "Filename already exists: " + file.getOriginalFilename();
//            logger.warn(message);
//            throw new IllegalArgumentException(message); // Throw exception or return a message as per your design
//        }
//
//        // Try to copy the file to the defined path
//        try {
//            Files.copy(file.getInputStream(), filePath);
//        } catch (IOException e) {
//            logger.error("IOException occurred while saving file: " + e.getMessage(), e);
//            throw new RuntimeException("Error saving the file", e);
//        }
//
//        // Create and save the ImageMetadataNew object
//        ImageMetadataNew imageMetadata = new ImageMetadataNew();
//        imageMetadata.setUserId(userId);
//        imageMetadata.setFileName(file.getOriginalFilename());
//        imageMetadata.setUploadDate(LocalDateTime.now());
//        imageMetadata.setDescriptions(file.getResource().getDescription());
//
//        // Save imageMetadata to your database
////        ImageMetadataRepository.save(imageMetadata);
//        imageMetadataRepository.save(imageMetadata);
//
//        logger.info("Files are saved successfully: " + imageMetadata);
//
//        return imageMetadata; // Return the saved metadata
//    }


    public ImageMetadataNew getProfileImage(Integer userId) {
        // Fetch the latest profile image from the database based on userId
        // For example:
        // return userRepository.findTopByUserIdOrderByUploadDateDesc(userId);

        return null; // Implement your logic to return the user's profile image
    }

    public void deleteProfileImage(Integer userId) {
        // Implement logic to delete the user's profile image from the database and storage
    }


}
