package com.act.Gakos.service;

import com.act.Gakos.entity.User;
import com.act.Gakos.entity.UserDetails;
import com.act.Gakos.repository.UserDetailsRepository;
import com.act.Gakos.repository.UserRepository; // Import the UserRepository
import com.act.Gakos.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository; // Inject the UserRepository

//    public BaseResponse<UserDetails> addUserDetails(UserDetails userDetails) {
//        try {
//            // Check if the user exists
//            Optional<User> user = userRepository.findById(userDetails.getUser().getId());
//            logger.info("Searching by user id", userDetails.getUser().getId());
//            if (user.isPresent()) {
//                UserDetails savedDetails = userDetailsRepository.save(userDetails);
//                logger.info("UserDetails added successfully: {}", savedDetails);
//                return new BaseResponse<>("UserDetails added successfully", true, savedDetails);
//            } else {
//                logger.warn("User not found for ID: {}", userDetails.getUser().getId());
//                return new BaseResponse<>("User not found", false, null);
//            }
//        } catch (Exception e) {
//            logger.error("Error adding UserDetails: {}", e.getMessage());
//            return new BaseResponse<>("Failed to add UserDetails: " + e.getMessage(), false, null);
//        }
//    }


//    public BaseResponse<UserDetails> addUserDetails(UserDetails userDetails) {
//        try {
//            // Check if the user exists using the ID from userDetails
//            Integer userId = userDetails.getUser() != null ? userDetails.getUser().getId() : null;
//
//            if (userId == null) {
//                logger.warn("User ID is null in UserDetails");
//                return new BaseResponse<>("User ID is missing", false, null);
//            }
//
//            Optional<User> userOptional = userRepository.findById(userId);
//            logger.info("Searching for user by ID: {}", userId);
//
//            if (userOptional.isPresent()) {
//                // Set the user in UserDetails before saving
//                userDetails.setUser(userOptional.get());
//                UserDetails savedDetails = userDetailsRepository.save(userDetails);
//                logger.info("UserDetails added successfully: {}", savedDetails);
//                return new BaseResponse<>("UserDetails added successfully", true, savedDetails);
//            } else {
//                logger.warn("User not found for ID: {}", userId);
//                return new BaseResponse<>("User not found", false, null);
//            }
//        } catch (Exception e) {
//            logger.error("Error adding UserDetails: {}", e.getMessage(), e);
//            return new BaseResponse<>("Failed to add UserDetails: " + e.getMessage(), false, null);
//        }
//    }

    // Add UserDetails
//    public BaseResponse<UserDetails> addUserDetails(UserDetails userDetails, Integer userId) {
//        try {
//            // Check if the User exists
//
//            logger.info("Searching for user with ID: {}", userId);
//            Optional<User> user = userRepository.findById(userId);
//            if (user.isPresent()) {
//                // Set the user reference and save UserDetails
//                UserDetails savedDetails = userDetailsRepository.save(userDetails);
//                logger.info("UserDetails saved successfully for user ID: {}", userId);
//                return new BaseResponse<>("UserDetails added successfully", true, savedDetails);
//            } else {
//                logger.warn("User not found for ID: {}", userId);
//                return new BaseResponse<>("User not found", false, null);
//            }
//        } catch (Exception e) {
//            logger.error("Error adding UserDetails: {}", e.getMessage(), e);
//            return new BaseResponse<>("Failed to add UserDetails: " + e.getMessage(), false, null);
//        }
//    }


    public BaseResponse<UserDetails> addUserDetails(UserDetails userDetails, Integer userId) {
        try {
            // Log the search operation
            logger.info("Searching for user with ID: {}", userId);
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userDetails.setUser(user); // Associate the user with userDetails

                // Save the UserDetails
                UserDetails savedDetails = userDetailsRepository.save(userDetails);
                logger.info("UserDetails saved successfully for user ID: {}", userId);
                return new BaseResponse<>("UserDetails added successfully", true, savedDetails);
            } else {
                logger.warn("User not found for ID: {}", userId);
                return new BaseResponse<>("User not found", false, null);
            }
        } catch (Exception e) {
            logger.error("Error adding UserDetails: {}", e.getMessage(), e);
            return new BaseResponse<>("Failed to add UserDetails: " + e.getMessage(), false, null);
        }
    }


    public BaseResponse<UserDetails> updateUserDetails(Long id, UserDetails userDetails) {
        try {
            Optional<UserDetails> existingDetails = userDetailsRepository.findById(id);
            if (existingDetails.isPresent()) {
//                userDetails.setId(id); // Ensure we're updating the correct entry
                UserDetails updatedDetails = userDetailsRepository.save(userDetails);
                logger.info("UserDetails updated successfully: {}", updatedDetails);
                return new BaseResponse<>("UserDetails updated successfully", true, updatedDetails);
            } else {
                logger.warn("UserDetails not found for ID: {}", id);
                return new BaseResponse<>("User details not found for the given ID", false, null);
            }
        } catch (Exception e) {
            logger.error("Error updating UserDetails: {}", e.getMessage());
            return new BaseResponse<>("Failed to update UserDetails: " + e.getMessage(), false, null);
        }
    }
}
