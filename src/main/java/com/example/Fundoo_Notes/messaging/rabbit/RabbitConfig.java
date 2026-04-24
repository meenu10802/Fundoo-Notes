package com.example.Fundoo_Notes.messaging.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String NOTE_EVENTS_QUEUE = "note.events.queue";

    @Bean
    public Queue noteEventsQueue() {
        return new Queue(NOTE_EVENTS_QUEUE, true);
    }
}
