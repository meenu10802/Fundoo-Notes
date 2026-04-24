package com.example.Fundoo_Notes.messaging.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteEventProducer {

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    public void sendNoteCreatedEvent(String message) {
        if (rabbitTemplate == null) {
            return;
        }
        rabbitTemplate.convertAndSend(RabbitConfig.NOTE_EVENTS_QUEUE, message);
    }
}
