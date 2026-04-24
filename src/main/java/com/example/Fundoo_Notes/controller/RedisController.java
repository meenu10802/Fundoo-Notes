package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class RedisController {

    @Autowired
    private RedisTokenService redisTokenService;

    @PostMapping("/otp/send")
    public ResponseEntity<String> cacheOtp(@RequestBody Map<String, String> request, Authentication authentication) {
        String otp = request.getOrDefault("otp", "123456");
        redisTokenService.cacheOtp(authentication.getName(), otp, 300);
        return ResponseEntity.ok("OTP cached for 5 minutes");
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request, Authentication authentication) {
        String cached = redisTokenService.getOtp(authentication.getName());
        String otp = request.getOrDefault("otp", "");
        if (cached != null && cached.equals(otp)) {
            return ResponseEntity.ok("OTP verified");
        }
        return ResponseEntity.badRequest().body("Invalid OTP");
    }
}
