package com.example.Fundoo_Notes.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.queue}")
    private String userQueue;

    @Value("${rabbitmq.note.queue}")
    private String noteQueue;

    @Value("${rabbitmq.user.routing-key}")
    private String userRoutingKey;

    @Value("${rabbitmq.note.routing-key}")
    private String noteRoutingKey;

    @Bean
    public TopicExchange fundooExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(userQueue, true);
    }

    @Bean
    public Queue noteCreationQueue() {
        return new Queue(noteQueue, true);
    }

    @Bean
    public Binding userRegistrationBinding() {
        return BindingBuilder
                .bind(userRegistrationQueue())
                .to(fundooExchange())
                .with(userRoutingKey);
    }

    @Bean
    public Binding noteCreationBinding() {
        return BindingBuilder
                .bind(noteCreationQueue())
                .to(fundooExchange())
                .with(noteRoutingKey);
    }
}