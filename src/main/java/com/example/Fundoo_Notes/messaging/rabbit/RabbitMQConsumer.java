package com.example.Fundoo_Notes.messaging;

import com.example.Fundoo_Notes.dto.NoteCreationEvent;
import com.example.Fundoo_Notes.dto.UserRegistrationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${rabbitmq.user.queue}")
    public void consumeUserRegistrationEvent(UserRegistrationEvent event) {
        System.out.println("RabbitMQ User Registration Event Received");
        System.out.println("Send welcome notification to: " + event.getEmail());
    }

    @RabbitListener(queues = "${rabbitmq.note.queue}")
    public void consumeNoteCreationEvent(NoteCreationEvent event) {
        System.out.println("RabbitMQ Note Creation Event Received");
        System.out.println("Notify userId " + event.getUserId() + " about note: " + event.getTitle());
    }
}