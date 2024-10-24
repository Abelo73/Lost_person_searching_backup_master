package com.act.Gakos.controller;

import com.act.Gakos.entity.UserDetails;
import com.act.Gakos.response.BaseResponse;
import com.act.Gakos.service.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-details")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

    @PostMapping
    public ResponseEntity<BaseResponse<UserDetails>> addUserDetails(@RequestBody UserDetails userDetails, @RequestParam Integer userId) {
        // Log the incoming request
        logger.info("User details request: {} ", userDetails);

        // Check if userId is provided
        if (userId == null) {
            logger.error("User ID is missing in the request");
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>("User ID is missing in the request", false, null)
            );
        }

        // Check if userDetails.getUser() is null
        if (userDetails.getUser() == null) {
            logger.error("User details object is missing user reference");
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>("User details object is missing user reference", false, null)
            );
        }

        // Log the user ID from userDetails
        logger.info("Received request to add user details for user ID: {}", userDetails.getUser().getId());

        // Call the service to add user details
        BaseResponse<UserDetails> response = userDetailsService.addUserDetails(userDetails, userId);
        if (response.isSuccess()) {
            logger.info("User details added successfully for user ID: {}", userDetails.getUser().getId());
        } else {
            logger.error("Failed to add user details for user ID: {}", userDetails.getUser().getId());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDetails>> updateUserDetails(@PathVariable Long id, @RequestBody UserDetails userDetails) {
        BaseResponse<UserDetails> response = userDetailsService.updateUserDetails(id, userDetails);
        return ResponseEntity.ok(response);
    }
}
