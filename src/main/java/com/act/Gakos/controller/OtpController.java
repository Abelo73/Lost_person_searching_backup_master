package com.act.Gakos.controller;

import com.act.Gakos.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5174")  // Allow requests from the frontend
@RequestMapping("/api/otp/")
public class OtpController {

    @Autowired
    private OtpService otpService;
    private final static Logger log = LoggerFactory.getLogger(OtpService.class);

    // Endpoint to send OTP
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        String otp = otpService.generateOtp();
        otpService.sendOtp(request.getEmail(), otp);
        return ResponseEntity.ok("OTP sent to " + request.getEmail());
    }

    // Endpoint to verify OTP
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP.");
        }
    }



    // DTO for send OTP request
    class OtpRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // DTO for verify OTP request
    class OtpVerifyRequest {
        private String email;
        private String otp;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

    }
}
