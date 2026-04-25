package com.example.Fundoo_Notes.messaging.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsMessagingService {

    private static final Logger log = LoggerFactory.getLogger(JmsMessagingService.class);
    public static final String NOTES_QUEUE = "fundoo.notes.jms.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    // ✅ PRODUCER
    public void send(String message) {
        jmsTemplate.convertAndSend(NOTES_QUEUE, message);
        log.info("📤 JMS sent message: {}", message);
    }

    // ✅ CONSUMER
    @JmsListener(destination = NOTES_QUEUE)
    public void receive(String message) {
        log.info("📥 JMS received message: {}", message);
    }
}