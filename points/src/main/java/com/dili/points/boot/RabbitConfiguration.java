package com.dili.points.boot;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Created by asiam on 2018/3/14 0014.
 */
@Configuration
public class RabbitConfiguration {

    public static final String DEFAULT_TOPIC_EXCHANGE = "diligrp.crm.topic";
    public static final String TOPIC_ROUTE_KEY = "crm.customer";
    public static final String TOPIC_QUEUE = "points-" + UUID.randomUUID();

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(DEFAULT_TOPIC_EXCHANGE, true, true);
    }


    @Bean
    public Queue randomQueue() {
        return new Queue(TOPIC_QUEUE, true, false, true);
    }

    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(randomQueue()).to(topicExchange()).with(TOPIC_ROUTE_KEY);
    }
}