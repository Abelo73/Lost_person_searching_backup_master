package com.act.Gakos.controller;

import com.act.Gakos.entity.ProfilePicture;
import com.act.Gakos.response.BaseResponse;
import com.act.Gakos.service.ProfilePictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ProfilePictureController {

    @Autowired
    private ProfilePictureService profilePictureService;

    private final static Logger logger = LoggerFactory.getLogger(ProfilePicture.class);


    @PostMapping("/{userId}/uploadProfilePicture")
    public ResponseEntity<BaseResponse<ProfilePicture>> uploadProfilePicture(
            @PathVariable Integer userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("dateOfPictureTaken") String dateOfPictureTakenStr) {

        try {
            logger.debug("Uploading profile picture for userId: {}, description: {}, location: {}", userId, description, location);

            // Validate file
            if (file.isEmpty()) {
                logger.warn("File is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>("File must not be empty", false));
            }

            // Validate description
            if (description == null || description.trim().isEmpty()) {
                logger.warn("Description is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>("Description must not be empty", false));
            }

            // Validate location
            if (location == null || location.trim().isEmpty()) {
                logger.warn("Location is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>("Location must not be empty", false));
            }

            // Validate and parse dateOfPictureTaken
            LocalDateTime dateOfPictureTaken;
            try {
                dateOfPictureTaken = LocalDateTime.parse(dateOfPictureTakenStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                logger.warn("Invalid date format: {}", dateOfPictureTakenStr);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new BaseResponse<>("Invalid date format", false));
            }

            // Save profile picture
            ProfilePicture profilePicture = profilePictureService.saveProfilePicture(userId, file, description, location, dateOfPictureTaken);
            logger.info("Profile picture entity created and saved for user: {}", userId);

            // Return success response
            return ResponseEntity.ok(new BaseResponse<>("Profile picture uploaded successfully", true, profilePicture));

        } catch (IOException e) {
            logger.error("File storage failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>("Failed to store file", false));
        } catch (RuntimeException e) {
            logger.error("Error occurred during upload: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(e.getMessage(), false));
        }
    }




//    @PostMapping("/{userId}/uploadProfilePicture")
//    public ResponseEntity<?> uploadProfilePicture(@PathVariable Integer userId,
//                                                  @RequestParam("file") MultipartFile file,
//                                                  @RequestParam("description") String description,
//                                                  @RequestParam("location") String location,
//                                                  @RequestParam("dateOfPictureTaken") String dateOfPictureTakenStr
//
//    ) {
//        try {
//            logger.debug("Uploading profile picture for userId: {}, description: {}, location: {}", userId, description, location);
//
//            if (file.isEmpty()) {
//                logger.warn("File is empty");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File must not be empty");
//            }
//            if (description == null || description.trim().isEmpty()) {
//                logger.warn("Description is empty");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Description must not be empty");
//            }
//            if (location == null || location.trim().isEmpty()) {
//                logger.warn("Location is empty");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Location must not be empty");
//            }
//
//            LocalDateTime dateOfPictureTaken;
//            try {
//                dateOfPictureTaken = LocalDateTime.parse(dateOfPictureTakenStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//            } catch (DateTimeParseException e) {
//                logger.warn("Invalid date format: {}", dateOfPictureTakenStr);
//                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
//            }
//
////            LocalDateTime dateOfPictureTaken = LocalDateTime.parse(dateOfPictureTakenStr, DateTimeFormatter.ISO_LOCAL_DATE);
//
//            ProfilePicture profilePicture = profilePictureService.saveProfilePicture(userId, file, description, location, dateOfPictureTaken);
//            logger.info("Profile picture entity created and saved for user: {}", userId);
//            return ResponseEntity.ok(profilePicture);
//        } catch (IOException e) {
//            logger.error("File storage failed", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store file");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            logger.error("Error occurred {} ============================", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }



    @GetMapping("/{userId}/profilePictures")
    public ResponseEntity<BaseResponse<Page<ProfilePicture>>> getProfilePictures(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = Sort.by("desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProfilePicture> profilePictures = profilePictureService.getProfilePicturesByUserId(userId, pageable);

        // If no profile pictures are found for the user
        if (profilePictures.isEmpty()) {
            BaseResponse<Page<ProfilePicture>> response = new BaseResponse<>(
                    "No profile pictures found for userId: " + userId,
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Return success response with profile pictures
        BaseResponse<Page<ProfilePicture>> response = new BaseResponse<>(
                "Profile pictures retrieved successfully",
                true,
                profilePictures
        );
        return ResponseEntity.ok(response);
    }




//    @GetMapping("/{userId}/profilePictures")
//    public ResponseEntity<Page<ProfilePicture>> getProfilePictures(
//            @PathVariable Integer userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "desc") String direction) {
//
//        Sort sort = Sort.by("desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<ProfilePicture> profilePictures = profilePictureService.getProfilePicturesByUserId(userId, pageable);
//
//        if (profilePictures.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        return ResponseEntity.ok(profilePictures);
//    }

    @PutMapping("/{userId}/profilePictures/{profilePictureId}")
    public ResponseEntity<BaseResponse<?>> updateProfilePicture(
            @PathVariable Integer userId,
            @PathVariable Integer profilePictureId,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String dateOfPictureTakenStr) {

        try {
            LocalDateTime dateOfPictureTaken = null;

            // Parse the date if provided
            if (dateOfPictureTakenStr != null) {
                try {
                    dateOfPictureTaken = LocalDateTime.parse(dateOfPictureTakenStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException e) {
                    logger.warn("Invalid date format: {}", dateOfPictureTakenStr);

                    // Return error response with BaseResponse
                    BaseResponse<String> response = new BaseResponse<>(
                            "Invalid date format",
                            false,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                }
            }

            // Call the service to update the profile picture
            ProfilePicture updatedProfilePicture = profilePictureService.updateProfilePicture(
                    userId, profilePictureId, file, description, location, dateOfPictureTaken);

            // Return success response with updated profile picture
            BaseResponse<ProfilePicture> response = new BaseResponse<>(
                    "Profile picture updated successfully",
                    true,
                    updatedProfilePicture
            );
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Log the error
            logger.error("Error occurred during update: {}", e.getMessage());

            // Return error response with BaseResponse
            BaseResponse<String> response = new BaseResponse<>(
                    e.getMessage(),
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (IOException e) {
            // Handle IOException by throwing runtime exception
            logger.error("File handling error", e);
            throw new RuntimeException(e);
        }
    }



//    @PutMapping("/{userId}/profilePictures/{profilePictureId}")
//    public ResponseEntity<?> updateProfilePicture(@PathVariable Integer userId,
//                                                  @PathVariable Integer profilePictureId,
//                                                  @RequestParam(required = false) MultipartFile file,
//                                                  @RequestParam(required = false) String description,
//                                                  @RequestParam(required = false) String location,
//                                                  @RequestParam(required = false) String dateOfPictureTakenStr) {
//        try {
//            LocalDateTime dateOfPictureTaken = null;
//            if (dateOfPictureTakenStr != null) {
//                try {
//                    dateOfPictureTaken = LocalDateTime.parse(dateOfPictureTakenStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//                } catch (DateTimeParseException e) {
//                    logger.warn("Invalid date format: {}", dateOfPictureTakenStr);
//                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid date format");
//                }
//            }
//            ProfilePicture updatedProfilePicture = profilePictureService.updateProfilePicture(userId, profilePictureId, file, description, location, dateOfPictureTaken);
//            return ResponseEntity.ok(updatedProfilePicture);
//        } catch (RuntimeException e) {
//            logger.error("Error occurred during update: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    @DeleteMapping("/{userId}/profilePictures/{profilePictureId}")
//    public ResponseEntity<?> deleteProfilePicture(@PathVariable Integer userId, @PathVariable Integer profilePictureId) {
//        try {
//            profilePictureService.deleteProfilePicture(userId, profilePictureId);
//            logger.info("Profile picture deleted for userId: {}", userId);
//            return ResponseEntity.ok("Profile picture deleted successfully");
//        } catch (IOException e) {
//            logger.error("Failed to delete profile picture file", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete profile picture");
//        } catch (RuntimeException e) {
//            logger.error("Error occurred while deleting profile picture: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }



    @DeleteMapping("/{userId}/profilePictures/{profilePictureId}")
    public ResponseEntity<BaseResponse<?>> deleteProfilePicture(
            @PathVariable Integer userId,
            @PathVariable Integer profilePictureId) {

        try {
            // Call the service to delete the profile picture
            profilePictureService.deleteProfilePicture(userId, profilePictureId);

            // Log the deletion
            logger.info("Profile picture deleted for userId: {}", userId);

            // Return success response with BaseResponse
            BaseResponse<String> response = new BaseResponse<>(
                    "Profile picture deleted successfully",
                    true,
                    null
            );
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            // Log the error
            logger.error("Failed to delete profile picture file", e);

            // Return error response with BaseResponse
            BaseResponse<String> response = new BaseResponse<>(
                    "Failed to delete profile picture",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (RuntimeException e) {
            // Log the error
            logger.error("Error occurred while deleting profile picture: {}", e.getMessage());

            // Return error response with BaseResponse
            BaseResponse<String> response = new BaseResponse<>(
                    e.getMessage(),
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



}



