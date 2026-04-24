package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.messaging.jms.JmsMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/jms")
public class JmsController {

    @Autowired
    private JmsMessagingService jmsMessagingService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "empty message");
        jmsMessagingService.send(message);
        return ResponseEntity.ok("JMS message sent");
    }
}
