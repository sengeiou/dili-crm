package com.dili.dp.boot;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by asiam on 2018/3/14 0014.
 */
@Configuration
@ConditionalOnExpression("'${mq.enable}'=='true'")
public class RabbitConfiguration {

    //积分MQ
    public static final String DEFAULT_TOPIC_EXCHANGE = "diligrp.points.topicExchange";
    public static final String TOPIC_ORDER_ROUTE_KEY = "diligrp.points.syncOrderKey";
    public static final String TOPIC_CATEGORY_ROUTE_KEY = "diligrp.points.syncCategoryKey";
    public static final String ORDER_TOPIC_QUEUE = "points.queue";
    public static final String CATEGORY_TOPIC_QUEUE = "category.queue";

    //CRM MQ
    public static final String CRM_TOPIC_EXCHANGE = "diligrp.crm.topicExchange";
    public static final String TOPIC_ROUTE_KEY = "diligrp.crm.addCustomerKey";
    public static final String TOPIC_QUEUE = "crm.queue";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(DEFAULT_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue randomOrderQueue() {
        return new Queue(ORDER_TOPIC_QUEUE, true, false, false);
    }

    @Bean
    public Queue randomCategoryQueue() {
        return new Queue(CATEGORY_TOPIC_QUEUE, true, false, false);
    }

    @Bean
    public Binding orderTopicBinding() {
        return BindingBuilder.bind(randomOrderQueue()).to(topicExchange()).with(TOPIC_ORDER_ROUTE_KEY);
    }

    @Bean
    public Binding categoryTopicBinding() {
        return BindingBuilder.bind(randomCategoryQueue()).to(topicExchange()).with(TOPIC_CATEGORY_ROUTE_KEY);
    }

    // -------------------  CRM   -----------------------------------------------

    @Bean
    public TopicExchange crmTopicExchange() {
        return new TopicExchange(CRM_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue randomQueue() {
        return new Queue(TOPIC_QUEUE, true, false, false);
    }

    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(randomQueue()).to(topicExchange()).with(TOPIC_ROUTE_KEY);
    }
}