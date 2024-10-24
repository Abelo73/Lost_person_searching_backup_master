package com.act.Gakos.controller;

import com.act.Gakos.config.JwtService;
import com.act.Gakos.dto.UserDTO;
import com.act.Gakos.entity.LoginAttempt;
import com.act.Gakos.entity.Role;
import com.act.Gakos.entity.User;
import com.act.Gakos.response.AuthenticationResponse;
import com.act.Gakos.response.BaseResponse;
//import com.act.Gakos.response.LoginResponse;

import com.act.Gakos.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ua_parser.Client;
import ua_parser.OS;
import ua_parser.Parser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationService authenticationService;
    private final  JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }


//
//    @GetMapping("/all")
//    public ResponseEntity<List<UserDTO>> getAllUsers(){
//        try {
//            List<UserDTO> users = authenticationService.users();
//            logger.info("All Users from Controller: {}", users);
//            return ResponseEntity.ok(users);
//        } catch (Exception e) {
//            logger.info("Error happen, error: {} ",e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }


    @GetMapping("/all")
    public BaseResponse<List<User>> getAllUsers(){
        try {
            List<User> users = authenticationService.users();
            return new BaseResponse<>("All users fetched successfully", true, users);
        }catch (Exception e){
            return new BaseResponse<>(e.getMessage(), false, null);
        }
    }

    @GetMapping("/all_users")
    public BaseResponse<Page<User>> allUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

            Page<User> users = authenticationService.getAllUsers(pageable);
            logger.info("Paginated Users from Controller: {}", users.getContent());

            return new BaseResponse<>("User retried", true, users);
        } catch (Exception e) {
            logger.error("Error happened: {}", e.getMessage());
            return new BaseResponse<>(e.getMessage(), false, null);
        }
    }



    @GetMapping("/search_by_search_term")
    public BaseResponse<Page<User>> searchUserBySearchTerm(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam("searchTerm") String searchTerm,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ){
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        Page<User> users = authenticationService.searchUserBySearchTerm(searchTerm, pageable);
        return new BaseResponse<>("User search found successfully", true, users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<User>> updateUser(
            @PathVariable Integer id,
            @RequestBody User updatedUser) {
        try {
            User user = authenticationService.updateUser(id, updatedUser);
            if (user != null) {
                return ResponseEntity.ok(new BaseResponse<>("User updated successfully", true, user));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponse<>("User not found", false, null));
            }
        } catch (Exception e) {
            logger.error("Error occurred during user update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>("An error occurred: " + e.getMessage(), false, null));
        }
    }




    @PostMapping("/register")
    public BaseResponse register(@RequestBody User request){
        try{
            authenticationService.register(request);
            String token = jwtService.generateToke(request);
            return new BaseResponse("Registration successfully.", true,token);
        }catch (Exception e){
            return new BaseResponse(e.getMessage(), false, null);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request){
//        return ResponseEntity.ok(authenticationService.authenticate(request));
//    }
//@PostMapping("/login")
//public ResponseEntity<AuthenticationResponse> login(@RequestBody User request) {
//    logger.info("Login request received for username: {}", request.getUsername());
//    AuthenticationResponse response = authenticationService.authenticate(request);
//    // Log response after authentication
//    logger.info("Login successful for username: {}", request.getUsername());
//    return ResponseEntity.ok(response);
//}


//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request, HttpServletRequest httpServletRequest) {
//        String ipAddress = httpServletRequest.getRemoteAddr(); // Get client IP address
//        String userAgent = httpServletRequest.getHeader("User-Agent"); // Get device details (User-Agent)
//        return ResponseEntity.ok(authenticationService.authenticate(request, ipAddress, userAgent));
//    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User loginRequest, HttpServletRequest request) {
//        String ipAddress = request.getRemoteAddr();  // Get the IP address of the user
//        String deviceDetails = request.getHeader("User-Agent");  // Get the device details (user-agent)
//
//        try {
//            // Call the authentication service to authenticate the user
//            String token = String.valueOf(authenticationService.authenticate(
//                    loginRequest.getUsername(),
//                    loginRequest.getPassword(),
//                    ipAddress,
//                    deviceDetails
//            ));
//            return ResponseEntity.ok().body(token);
//
//        } catch (RuntimeException ex) {
//            // Return an error response if authentication fails
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + ex.getMessage());
//        }
//    }


    @PostMapping("/login")
    public BaseResponse login(@RequestBody User loginRequest, HttpServletRequest request) throws IOException {
        String ipAddress = request.getRemoteAddr();  // Get the IP address of the user
        String deviceDetails = request.getHeader("User-Agent");  // Get the device details (user-agent)
//        UserAgent userAgent = UserAgent.parseUserAgentString(deviceDetails);
//        String operatingSystem = userAgent.getOperatingSystem().getName();  // Get the OS name
//        String browser = userAgent.getBrowser().getName();  // Get the browser name

        Parser uaParser = new Parser();
        Client client = uaParser.parse(deviceDetails);  // Parse the User-Agent string
        OS os = client.os;  // Get the OS details
        String operatingSystem = os.family;  // OS family (e.g., Windows, macOS)
        String browser = client.userAgent.family;  // Browser family (e.g., Chrome, Firefox)

        try {
            // Call the authentication service to authenticate the user
            AuthenticationResponse authResponse = authenticationService.authenticate(
                    loginRequest.getUsername(),
                    loginRequest.getPassword(),
                    operatingSystem,
                    browser,
                    ipAddress,
                    deviceDetails
            );

            // Extract the token from the AuthenticationResponse and return it
            String token = authResponse.getToken();
            logger.info("Detected OS: " + operatingSystem + ", Browser: " + browser);

            return new BaseResponse("Logg in successfully", true, token);

        } catch (RuntimeException ex) {
            // Return an error response if authentication fails
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + ex.getMessage());
            return new BaseResponse(ex.getMessage(), false, null);
        }
    }

//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User loginRequest, HttpServletRequest request) throws IOException {
//        String ipAddress = request.getRemoteAddr();  // Get the IP address of the user
//        String deviceDetails = request.getHeader("User-Agent");  // Get the device details (user-agent)
////        UserAgent userAgent = UserAgent.parseUserAgentString(deviceDetails);
////        String operatingSystem = userAgent.getOperatingSystem().getName();  // Get the OS name
////        String browser = userAgent.getBrowser().getName();  // Get the browser name
//
//        Parser uaParser = new Parser();
//        Client client = uaParser.parse(deviceDetails);  // Parse the User-Agent string
//        OS os = client.os;  // Get the OS details
//        String operatingSystem = os.family;  // OS family (e.g., Windows, macOS)
//        String browser = client.userAgent.family;  // Browser family (e.g., Chrome, Firefox)
//
//        try {
//            // Call the authentication service to authenticate the user
//            AuthenticationResponse authResponse = authenticationService.authenticate(
//                    loginRequest.getUsername(),
//                    loginRequest.getPassword(),
//                    operatingSystem,
//                    browser,
//                    ipAddress,
//                    deviceDetails
//            );
//
//            // Extract the token from the AuthenticationResponse and return it
//            String token = authResponse.getToken();
//            logger.info("Detected OS: " + operatingSystem + ", Browser: " + browser);
//
//            return ResponseEntity.ok().body(token);
//
//        } catch (RuntimeException ex) {
//            // Return an error response if authentication fails
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + ex.getMessage());
//        }
//    }


    // Endpoint to get login attempts (only accessible to ADMIN or SUPER_ADMIN)
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
//    @GetMapping("/loginAttempts")
//    public ResponseEntity<List<LoginAttempt>> getAllLoginAttempts() {
//        List<LoginAttempt> loginAttempts = authenticationService.getAllLoginAttempts();
//        return ResponseEntity.ok(loginAttempts);
//    }


//    @GetMapping("/loginAttempts")
//    public Page<LoginAttempt> getLoginAttempts(@RequestParam(defaultValue = "0") int page,
//                                               @RequestParam(defaultValue = "5") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return authenticationService.getAllLoginAttempts(pageable);
//    }


//    @GetMapping("/loginAttempts")
//    public Page<LoginAttempt> getLoginAttempts(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "all") String success, // new filter criteria
//            @RequestParam(defaultValue = "desc") String sortDirection // sorting order
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), "timestamp"));
//
//        // Handle filtering by success status
//        if (success.equalsIgnoreCase("yes")) {
//            logger.info("Success login attempts: "+ authenticationService.getLoginAttemptsBySuccess(true, pageable));
//            return authenticationService.getLoginAttemptsBySuccess(true, pageable);
//        } else if (success.equalsIgnoreCase("no")) {
//            logger.info("Success login attempts: "+ authenticationService.getLoginAttemptsBySuccess(false, pageable));
//
//            return authenticationService.getLoginAttemptsBySuccess(false, pageable);
//        } else {
//            // Return all if success filter is not applied
//            logger.info("Success login attempts: "+ authenticationService.getLoginAttemptsBySuccess(true, pageable));
//
//            return authenticationService.getAllLoginAttempts(pageable);
//        }
//    }



    @GetMapping("/loginAttempts")
    public ResponseEntity<Page<LoginAttempt>> getLoginAttempts(
            @PageableDefault(size = 5) Pageable pageable,
            @RequestParam(required = false) Boolean success,
            @RequestParam(required = false) String operatingSystem,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        // Sort by timestamp
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(sortDirection), "timestamp"));

        Page<LoginAttempt> attempts;
        if (success != null && operatingSystem != null) {
            attempts = authenticationService.getLoginAttemptsBySuccessAndOS(success, operatingSystem, pageable);
        } else if (success != null) {
            attempts = authenticationService.getLoginAttemptsBySuccess(success, pageable);
        } else if (operatingSystem != null) {
            attempts = authenticationService.getLoginAttemptsByOS(operatingSystem, pageable);
        } else {
            attempts = authenticationService.getAllLoginAttempts(pageable);
        }

        return ResponseEntity.ok(attempts);
    }




    @GetMapping("/count")
    public BaseResponse getTotalUsers(){
        long totalUsers = authenticationService.getTotalUsers();
//        return ResponseEntity.ok(totalUsers);
        return new BaseResponse("Total user count is fetched successfully.", true, totalUsers);
    }

    @GetMapping("/loginAttempts/count")
    public BaseResponse getTotalLoginAttempts(){
        long totalLoginAttempts = authenticationService.getTotalLoginAttempts();
//        return ResponseEntity.ok(totalLoginAttempts);
        return new BaseResponse("Total Login attempts successfully fetched.", true, totalLoginAttempts);
    }



    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        try {
            List<User> users = authenticationService.users();
            logger.info("All users fetched: {}", users);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.info("Error happen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @GetMapping("/{id}")
//    public BaseResponse<UserDTO> getUserById(@PathVariable Integer id) {
//        try {
//            UserDTO userDTO = authenticationService.
//                    findById(id);
//            return new BaseResponse<>("user found successfully", true, userDTO);
//        } catch (Exception e) {
//            return new BaseResponse<>(e.getMessage(), false, null);
//        }
//    }

    @GetMapping("/user/{id}")
    public BaseResponse<Optional<User>> getUserByUserId(@PathVariable Integer id){
        try {
            Optional<User> user = authenticationService.getUserById(id);
            return new BaseResponse<>("User found successfully.", true, user);
        }catch (Exception e){
            return new BaseResponse<>(e.getMessage(), false, null);
        }
    }


    @GetMapping("/search/by_username/{username}")
    public BaseResponse<Page<User>> getByUsername(@PathVariable String username,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<User> users = authenticationService.getUserByUsername(username, pageable);

            if (!users.isEmpty()) {
                return new BaseResponse<>("Users found", true, users);
            } else {
                return new BaseResponse<>("No users found with the provided username", false, null);
            }
        } catch (Exception e) {
            return new BaseResponse<>("An error occurred: " + e.getMessage(), false, null);
        }
    }




//    @GetMapping("/findById/{id}")
//    public BaseResponse<UserDTO> getUsersById(@PathVariable Integer id){
//        try {
//            UserDTO userDTO = authenticationService.getUserById(id).orElseThrow(()->new RuntimeException("User not found"));
//            return new BaseResponse<>("User found successfully.", true, userDTO);
//        }catch (Exception e){
//            return new BaseResponse<>(e.getMessage(), false, null);
//        }
//    }


    @GetMapping("/search/by_role")
    public BaseResponse<Page<User>> getUserByRole(@RequestParam Role role,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy
                                                  ){
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<User> users = authenticationService.getUserByRole(role, pageable);
            if (!users.isEmpty()) {
                return new BaseResponse<>("Users found", true, users);
            } else {
                return new BaseResponse<>("No users found with the provided username", false, null);
            }
        }catch (Exception e){
            return new BaseResponse<>(e.getMessage(), false, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            authenticationService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
//        try {
//            authenticationService.sendPasswordResetToken(email);
//
//            return ResponseEntity.ok("Password reset token sent to email.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }

//    @PostMapping("/reset-password/{token}")
//    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody String newPassword) {
//        try {
//            authenticationService.resetPassword(token, newPassword);
//            return ResponseEntity.ok("Password reset successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }

}
