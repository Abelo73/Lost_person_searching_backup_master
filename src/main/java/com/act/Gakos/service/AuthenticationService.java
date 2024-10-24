package com.act.Gakos.service;

import com.act.Gakos.config.JwtService;
import com.act.Gakos.dto.UserDTO;
import com.act.Gakos.entity.LoginAttempt;
import com.act.Gakos.entity.Role;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.LoginAttemptRepository;
import com.act.Gakos.repository.UserRepository;
import com.act.Gakos.response.AuthenticationResponse;
import com.act.Gakos.response.BaseResponse;
//import com.act.Gakos.response.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository repository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    private final EmailService emailService;

    public AuthenticationService(UserRepository repository, LoginAttemptRepository loginAttemptRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.repository = repository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }




    public BaseResponse register(User request){

        Optional<User> existingUser = repository.findByUsername(request.getUsername());
        if (existingUser.isPresent()){
            throw new IllegalArgumentException("Username is already taken");
        }

        validateUser(request);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMiddleName(request.getMiddleName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = repository.save(user);
        String token = jwtService.generateToke(user);

//        return new AuthenticationResponse(token);
        return new BaseResponse("Registration successfully", true, token);

    }

    public void validateUser(User user){
        if (user.getFirstName().isEmpty()){
            throw new RuntimeException("FirstName can not be empty");
        } else if (user.getLastName().isEmpty()) {
            throw new RuntimeException("Last name can not be empty");
        } else if (user.getUsername().isEmpty() || user.getUsername() == null) {
            throw new RuntimeException("Username can not be empty");
        } else if (user.getPassword().isEmpty() || user.getPassword() ==null) {
            throw new RuntimeException("Password can not be empty");
        }
    }

//    public AuthenticationResponse authenticate(User request){
//
//        validateLogin(request);
//
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    request.getUsername(),
//                    request.getPassword()
//            ));
//        }catch (java.lang.Exception e){
//            throw new RuntimeException("Invalid username or password");
//        }
//
//
//        User user = repository.findByUsername(request.getUsername()).orElseThrow();
//        String token = jwtService.generateToke(user);
//
//        return new AuthenticationResponse(token);
//    }

//    public AuthenticationResponse authenticate(User request){
//        validateLogin(request);
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//            logger.info("Login attempt successful for user: {}", request.getUsername());
//        } catch (Exception e) {
//            logger.error("Login attempt failed for user: {} due to {}", request.getUsername(), e.getMessage());
//            throw new RuntimeException("Invalid username or password");
//        }
//
//        // Retrieve user and generate token
//        User user = repository.findByUsername(request.getUsername()).orElseThrow();
//        String token = jwtService.generateToke(user);
//
//        // Log token generation
//        logger.info("Generated JWT token for user: {}", request.getUsername());
//
//        return new AuthenticationResponse(token);
//    }


//    public AuthenticationResponse authenticate(User request, String ipAddress, String deviceDetails) {
//        boolean success = false;
//
//        // Validate login credentials
//        validateLogin(request);
//
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    request.getUsername(),
//                    request.getPassword()
//            ));
//            success = true; // Authentication succeeded
//        } catch (Exception e) {
//            success = false; // Authentication failed
//            throw new RuntimeException("Invalid username or password");
//        }
//
//        // Save login attempt log
//        logLoginAttempt(request.getUsername(), success, ipAddress, deviceDetails);
//
//        User user = repository.findByUsername(request.getUsername()).orElseThrow();
//        String token = jwtService.generateToke(user);
//
//        return new AuthenticationResponse(token);
//    }
//
//    private void logLoginAttempt(String username, boolean success, String ipAddress, String deviceDetails) {
//        LoginAttempt loginAttempt = new LoginAttempt();
//        loginAttempt.setUsername(username);
//        loginAttempt.setSuccess(success);
//        loginAttempt.setIpAddress(ipAddress);
//        loginAttempt.setDeviceDetails(deviceDetails);
//        loginAttempt.setTimestamp(LocalDateTime.now());
//
//        // Save the login attempt in the database
//        loginAttemptRepository.save(loginAttempt);
//    }


    public AuthenticationResponse authenticate(String username, String password, String operatingSystem, String ipAddress, String deviceDetails, String details) {
        try {
            // Try to authenticate the user with the provided credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Log the successful login attempt
            logLoginAttempt(username, operatingSystem, ipAddress, deviceDetails, true);

            // If authentication is successful, return a token (JWT or session token)
            // This part depends on your implementation.
            // If authentication is successful, find the user and generate the token
            User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtService.generateToke(user);

            // Return the AuthenticationResponse containing the token
            return new AuthenticationResponse(token);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            // Log the failed login attempt
            logLoginAttempt(username,operatingSystem, ipAddress, deviceDetails, false);

            // Re-throw the exception or return an error message
            throw new RuntimeException("Invalid username or password");
        }
    }

    // Method to save login attempt to the database
    private void logLoginAttempt(String username, String operatingSystem, String ipAddress, String deviceDetails, boolean success) {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUsername(username);
        loginAttempt.setIpAddress(ipAddress);
        loginAttempt.setDeviceDetails(deviceDetails);
        loginAttempt.setBrowser(loginAttempt.getBrowser());
        loginAttempt.setOperatingSystem(operatingSystem);
        loginAttempt.setSuccess(success);
        loginAttempt.setTimestamp(LocalDateTime.now());

        loginAttemptRepository.save(loginAttempt);
    }


//    public List<LoginAttempt> getAllLoginAttempts(){
//        return loginAttemptRepository.findAll();
//    }
//
    public void validateLogin(User user){
        if (user.getUsername() == null || user.getUsername().isEmpty()){
            throw new RuntimeException("Username can not be empty");
        }
        if (user.getPassword() ==  null || user.getPassword().isEmpty()){
            throw new RuntimeException("Password can not be empty");
        }
    }


//    public Page<LoginAttempt> getAllLoginAttempts(Pageable pageable){
//        return loginAttemptRepository.findAll(pageable);
//    }

    public List<User> users(){
        List<User> users = repository.findAll();
        return users;
    }
//    public List<UserDTO> users(){
//
//        List<UserDTO> users = repository.findAll();
//        logger.info("Fetched Uses: "+ users);
//        return users;
//    }

    public Page<User> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }

//    public Page<User> getAllUsers(Pageable pageable) {
//        // Call the new repository method to fetch users with profile pictures
//        return repository.findAllUsersWithProfilePictures(pageable);
//    }




    public UserDTO findById(Integer id) {
        Optional<User> existingUser = repository.findById(id);
        if (existingUser.isPresent()) {
            return convertToDTO(existingUser.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }



    public void deleteUser(Integer id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(id);
            logger.info("User with ID {} deleted successfully", id);
        } else {
            throw new RuntimeException("User Not Found");
        }
    }


    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setLastName(user.getLastName());
        userDTO.setAddressDetails(user.getAddressDetails());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setFatherDetails(user.getFatherDetails());
        userDTO.setProfileInfo(user.getProfileInfo());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public Page<User> getUserByUsername(String username, Pageable pageable) {
        return repository.findAllByUsername(username, pageable);
    }


    public Page<User> getUserByRole(Role role, Pageable pageable){
        return  repository.findByRole(role, pageable);
    }

    public User updateUser(Integer id, User updatedUser) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update user fields (you can selectively update fields as needed)
//            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setFirstName(updatedUser.getFirstName());
            user.setMiddleName(updatedUser.getMiddleName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setAddressDetails(updatedUser.getAddressDetails());
            user.setUpdatedAt(LocalDateTime.now());
            user.setProfileInfo(updatedUser.getProfileInfo());
            user.setUserDetails(updatedUser.getUserDetails());
            user.setTokenExpiration(updatedUser.getTokenExpiration());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            // Add any other fields you wish to update

            // Save the updated user back to the repository
            return repository.save(user);
        }
        return null; // Return null if the user does not exist
    }

//    public Optional<User> getUserById(Integer id) {
//        return repository.findById(id);
//    }

    public Optional<User> getUserById(Integer id){
        return repository.findById(id);
    }


//    public Optional<UserDTO> getUserById(Integer id){
//        var user = repository.findById(id);
//        return Optional.of(convertToDTO(user.get()));
//    }

    public long getTotalUsers() {
        return repository.count();
    }

    public long getTotalLoginAttempts() {
        return loginAttemptRepository.count();
    }

    public Page<LoginAttempt> getLoginAttemptsBySuccess(boolean success, Pageable pageable) {
        return loginAttemptRepository.findBySuccess(success, pageable);
    }

//    public Page<LoginAttempt> getAllLoginAttempts(Pageable pageable) {
//        return loginAttemptRepository.findAll(pageable);
//    }

    public Page<LoginAttempt> getAllLoginAttempts(Pageable pageable) {
        return loginAttemptRepository.findAll(pageable);
    }

    public Page<LoginAttempt> getLoginAttemptsBySuccess(Boolean success, Pageable pageable) {
        return loginAttemptRepository.findBySuccess(success, pageable);
    }

    public Page<LoginAttempt> getLoginAttemptsByOS(String operatingSystem, Pageable pageable) {
        return loginAttemptRepository.findByOperatingSystem(operatingSystem, pageable);
    }

    public Page<LoginAttempt> getLoginAttemptsBySuccessAndOS(Boolean success, String operatingSystem, Pageable pageable) {
        return loginAttemptRepository.findBySuccessAndOperatingSystem(success, operatingSystem, pageable);
    }

    public Page<User> searchUserBySearchTerm(String searchTerm, PageRequest pageable) {
        return repository.searchBySearchTerms(searchTerm, pageable);
    }

//
//    public void sendPasswordResetToken(String email) {
//        User user = repository.findByUsername(email).orElseThrow(() -> new RuntimeException("User not found"));
//
//        String token = jwtService.generateToke(user);
//        logger.info("Generated password reset token for user {}: {}", email, token);
//
//
//
//        logger.info("Generated password reset token for user {}: {}", email, token);
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Password Reset Request");
//        message.setText("Here is your password reset token: " + token);
//
//        emailService.sendEmail(email, token);
//        logger.info("Password reset token sent to user {}.", email);
//        // You need an email service to actually send the email
//    }

//    public void resetPassword(String token, String newPassword) {
//        logger.info("Received password reset request with token: {}", token);
//
//        if (jwtService.isTokenIsExpired(token)) {
//            logger.error("Password reset token is expired.");
//            throw new RuntimeException("Token is expired");
//        }
//
//        Claims claims = jwtService.extractClaims(token, Function.identity());
//        String username = claims.getSubject();
//        logger.info("Username extracted from token: {}", username);
//
//        if (username == null) {
//            logger.error("Username extracted from token is null.");
//            throw new RuntimeException("Invalid token claims");
//        }
//
//        User user = repository.findByUsername(username).orElseThrow(() -> {
//            logger.error("User with username {} not found.", username);
//            return new RuntimeException("User not found");
//        });
//
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        logger.debug("New Encrypted Password: {}", encodedPassword);
//
//        user.setPassword(encodedPassword);
//        repository.save(user);
//        logger.info("Password for user with username {} has been reset successfully.", username);
//    }


}
