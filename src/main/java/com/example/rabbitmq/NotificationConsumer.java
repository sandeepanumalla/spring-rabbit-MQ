package com.example.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "queueName")
    public void receiveNotification(NotificationDTO notificationDTO) {

//        NotificationDTO notification = objectMapper.readValue(jsonPayload, NotificationDTO.class);
        System.out.println("Received notification: " + notificationDTO.toString());
    }
}
