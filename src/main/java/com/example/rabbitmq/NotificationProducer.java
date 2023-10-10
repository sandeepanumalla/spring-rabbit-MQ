package com.example.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;


@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(NotificationDTO notification) throws JsonProcessingException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(notification);

            MessageProperties messageProperties = new MessageProperties();
            // Set any message properties if needed
            // messageProperties.setHeader("key", "value");

            Message message = new Message(jsonMessage.getBytes(), messageProperties);
            rabbitTemplate.send("myDirectExchange", "routingKey", message);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
}
