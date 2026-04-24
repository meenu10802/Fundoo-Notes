package com.example.Fundoo_Notes.messaging.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NoteEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NoteEventConsumer.class);

    @RabbitListener(queues = RabbitConfig.NOTE_EVENTS_QUEUE)
    public void consume(String message) {
        log.info("RabbitMQ received event: {}", message);
    }
}
