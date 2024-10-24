package com.act.Gakos.controller;

import com.act.Gakos.entity.ImageMetadata;
import com.act.Gakos.entity.ImageMetadataNew;
import com.act.Gakos.repository.UserRepository;
import com.act.Gakos.request.UploadedFileMetadata;
import com.act.Gakos.response.BaseResponse;
import com.act.Gakos.response.ImageBaseResponse;
import com.act.Gakos.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    private static final String STORAGE_DIRECTORY = "/home/abel/eclipse-workspace/Desktop/douwnloadedImage";
    /**
     * Endpoint to delete a user by ID.
     *
     * @param id The ID of the user to be deleted.
     * @return ResponseEntity with a message about the operation result.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

//    @PostMapping("/reset-password")
//    public ResponseEntity<BaseResponse<String>> resetPassword(@RequestBody PasswordResetRequest request) {
//        try {
//            BaseResponse<String> response = userService.resetPassword(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }



    // Upload multiple personal images
//    @PostMapping("/{userId}")
//    public ResponseEntity<String> uploadPersonalImages(
//            @PathVariable Integer userId,
//            @RequestParam("personalImages") List<MultipartFile> personalImages) throws IOException {
//
//        userService.uploadPersonalImages(userId, personalImages);
//        return ResponseEntity.ok("Personal images uploaded successfully.");
//    }


//    @PostMapping("/uploadPersonalImage/{userId}")
//    public ImageBaseResponse uploadFiles(@PathVariable Integer userId,
//                                         @RequestParam("files") List<MultipartFile> filesToSave) {
//        try {
//            userService.saveFiles(filesToSave, userId);  // Call to service to save the files
//            log.info("Saved successfully");
//            return new ImageBaseResponse("File uploaded successfully",true, filesToSave);
//        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
//            return new ImageBaseResponse(e.getMessage(),false, null);
//        } catch (NullPointerException e) {
//            log.info(e.getMessage());
//            return new ImageBaseResponse("No files to upload", false, null);
//        } catch (SecurityException e) {
//            log.info(e.getMessage());
//            return new ImageBaseResponse("Invalid file name or path", false, null);
//        } catch (IOException e) {
//            log.info(e.getMessage());
//            return new ImageBaseResponse("Error saving the files", false, null);
//        }
//    }


    @PostMapping("/uploadPersonalImage/{userId}")
    public ImageBaseResponse uploadFiles(@PathVariable Integer userId,
                                         @RequestParam("files") List<MultipartFile> filesToSave) {
        try {
            // Call to service to save the files
            userService.saveFiles(filesToSave, userId);
            log.info("Files saved successfully");

            // Convert MultipartFile to UploadedFileMetadata
            List<UploadedFileMetadata> uploadedFilesMetadata = filesToSave.stream()
                    .map(file -> new UploadedFileMetadata(file.getOriginalFilename(), file.getSize(), file.getContentType(),file.getResource().getDescription() ))
                    .collect(Collectors.toList());

            return new ImageBaseResponse("Files uploaded successfully", true, uploadedFilesMetadata);
        } catch (IllegalArgumentException e) {
            log.warn("IllegalArgumentException: {}", e.getMessage());
            return new ImageBaseResponse(e.getMessage(), false, null);
        } catch (NullPointerException e) {
            log.warn("NullPointerException: {}", e.getMessage());
            return new ImageBaseResponse("No files to upload", false, null);
        } catch (SecurityException e) {
            log.warn("SecurityException: {}", e.getMessage());
            return new ImageBaseResponse("Invalid file name or path", false, null);
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
            return new ImageBaseResponse("Error saving the files", false, null);
        }
    }
    @PostMapping("/uploadProfileImage/{userId}")
    public ResponseEntity<ImageBaseResponse> uploadProfileImage(@PathVariable Integer userId,
                                                                @RequestParam("file") MultipartFile file) {

        log.info("Received file: Name = {}, Size = {}, Type = {}", file.getOriginalFilename(), file.getSize(), file.getContentType());

        try {
            // Call to service to save the profile image
            ImageMetadataNew savedImage = userService.saveProfileImage(userId, file);
            log.info("Profile image uploaded successfully: {}", savedImage.getFileName());

            // Wrap the saved image in a list for the response
            List<UploadedFileMetadata> imageData = List.of();

            // Create response with image metadata
            ImageBaseResponse response = new ImageBaseResponse(
                    "Profile image uploaded successfully",
                    true,
                    imageData // This should be a List<ImageMetadataNew>
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn("IllegalArgumentException: {}", e.getMessage());
            return new ResponseEntity<>(new ImageBaseResponse(e.getMessage(), false, null), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
            return new ResponseEntity<>(new ImageBaseResponse("Error saving the profile image", false, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





//    @PostMapping("/uploadProfileImage/{userId}")
//    public ResponseEntity<ImageBaseResponse> uploadProfileImage(@PathVariable Integer userId,
//                                                                @RequestParam("file") MultipartFile file) {
//
//        log.info("Received file: Name = {}, Size = {}, Type = {}", file.getOriginalFilename(), file.getSize(), file.getContentType());
//
//        try {
//            // Call to service to save the profile image
//            ImageMetadataNew savedImage = userService.saveProfileImage(userId, file);
//            log.info("Profile image uploaded successfully: {}", savedImage.getFileName());
//
//            // Create response with image metadata
//            ImageBaseResponse response = new ImageBaseResponse(
//                    "Profile image uploaded successfully",
//                    true,
//                    List.of() // Include the saved image metadata in the response
//            );
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            log.warn("IllegalArgumentException: {}", e.getMessage());
//            return new ResponseEntity<>(new ImageBaseResponse(e.getMessage(), false, null), HttpStatus.BAD_REQUEST);
//        } catch (IOException e) {
//            log.error("IOException: {}", e.getMessage());
//            return new ResponseEntity<>(new ImageBaseResponse("Error saving the profile image", false, null), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



//    // Endpoint to upload a profile image
//    @PostMapping("/uploadProfileImage/{userId}")
//    public ResponseEntity<ImageBaseResponse> uploadProfileImage(@PathVariable Integer userId,
//                                                                @RequestParam("file") MultipartFile file) {
//
//        log.info("Received file: Name = {}, Size = {}, Type = {}", file.getOriginalFilename(), file.getSize(), file.getContentType());
//
//        try {
//            // Call to service to save the profile image
//            ImageMetadataNew savedImage = userService.saveProfileImage(userId, file);
//            log.info("Profile image uploaded successfully: {}", savedImage.getFileName());
//
//            return new ResponseEntity<>(new ImageBaseResponse("Profile image uploaded successfully", true, List.of()), HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            log.warn("IllegalArgumentException: {}", e.getMessage());
//            return new ResponseEntity<>(new ImageBaseResponse(e.getMessage(), false, null), HttpStatus.BAD_REQUEST);
//        } catch (IOException e) {
//            log.error("IOException: {}", e.getMessage());
//            return new ResponseEntity<>(new ImageBaseResponse("Error saving the profile image", false, null), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



//    @GetMapping("/getPersonalImages/{userId}")
//    public BaseResponse<List<ImageMetadata>> getPersonalImages(@PathVariable Integer userId) {
//        try {
//            // Call service method to get the user's images
//            List<ImageMetadata> imageMetadataList = userService.getPersonalImagesByUserId(userId);
//
//            if (imageMetadataList.isEmpty()) {
//                return new BaseResponse<>("No images found for the user", true, null);
//            }
//
//            return new BaseResponse<>("Images retrieved successfully", true, imageMetadataList);
//        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
//            return new BaseResponse<>(e.getMessage(), false, null);
//        }
//    }


    @GetMapping("/getPersonalImages/{userId}")
    public BaseResponse<List<ImageMetadataNew>> getPersonalImages(@PathVariable Integer userId) {
        try {
            List<ImageMetadataNew> imageMetadataList = userService.getPersonalImagesByUserId(userId);

            if (imageMetadataList.isEmpty()) {
                return new BaseResponse<>("No images found for the user", true, null);
            }

            // Get the first image metadata
            ImageMetadataNew firstImageMetadata = imageMetadataList.get(0);

            // Log or process the first image as needed
            log.info("First image retrieved: {}", firstImageMetadata.getFileName());

            return new BaseResponse<>("Images retrieved successfully", true, imageMetadataList);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            return new BaseResponse<>(e.getMessage(), false, null);
        }
    }



//    @GetMapping("/downloadImage/{fileName}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
//        try {
//            Path filePath = Paths.get(STORAGE_DIRECTORY + fileName);
//            Resource resource = (Resource) new UrlResource(filePath.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } else {
//                throw new RuntimeException("Could not read the file!");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("File not found: " + e.getMessage());
//        }
//    }
//

    // Upload multiple additional images
//    @PostMapping("/oldImage/{userId}/")
//    public ResponseEntity<String> uploadAdditionalImages(
//            @PathVariable Integer userId,
//            @RequestParam("additionalImages") List<MultipartFile> additionalImages) throws IOException {
//
//        userService.uploadAdditionalImages(userId, additionalImages);
//        return ResponseEntity.ok("Additional images uploaded successfully.");
//    }


    // Request password reset
    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse<String>> requestPasswordReset(@RequestBody String username) {


        BaseResponse<String> response = userService.requestPasswordReset(username);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // Reset password using token
    @PostMapping("/reset-password/{token}")
    public ResponseEntity<BaseResponse<String>> resetPassword(@PathVariable String token, @RequestBody String newPassword) {
        BaseResponse<String> response = userService.resetPassword(token, newPassword);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }




}
