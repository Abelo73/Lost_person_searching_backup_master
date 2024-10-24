
package com.act.Gakos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> otpStorage = new HashMap<>();  // Store OTP temporarily




    private final static Logger log = LoggerFactory.getLogger(OtpService.class);






    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);  // Generate a 6-digit OTP
        return String.valueOf(otp);
    }

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);

        // Store OTP with email for verification (for simplicity, storing in memory)
        otpStorage.put(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }


    // Method to generate random OTP
    public String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        log.info("OTP : " + otp);
        return otp.toString();
    }
    public void sendSmsOtp(String phoneNumber) {
        String otp = generateOtp(6); // Generate a 6-digit OTP
        String message = "Your OTP is: " + otp;

        // Log the details for debugging
        log.info("Generated OTP: {} for phoneNumber: {}", otp, phoneNumber);
        log.info("Sending SMS with message: {}", message);

    }

}
