package com.act.Gakos.service;

import com.act.Gakos.entity.FatherDetails;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.UserRepository;
import com.act.Gakos.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FatherService {

    private static final Logger logger = LoggerFactory.getLogger(FatherService.class);
    private final UserRepository userRepository;

    public BaseResponse<User> addFatherToUser(String username, FatherDetails father) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFatherDetails(father); // Set father to user
                userRepository.save(user); // Save user with father

                logger.info("Successfully added father for user: {}", username);
                return new BaseResponse<>("Father added successfully", true, user);
            } else {
                logger.warn("User not found: {}", username);
                return new BaseResponse<>("User not found", false, null);
            }
        } catch (Exception e) {
            logger.error("Error occurred while adding father: {}", e.getMessage());
            return new BaseResponse<>("An error occurred: " + e.getMessage(), false, null);
        }
    }
}
