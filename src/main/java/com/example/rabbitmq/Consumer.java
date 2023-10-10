package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final Channel channel;

    @Autowired
    public Consumer(Channel channel) {
        this.channel = channel;
    }

    public void receiveMessage() throws IOException {
        String queueName = "my_queue";
        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // Set up a consumer to receive messages from the queue
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            // Manually acknowledge the message after processing
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        // Start consuming messages from the queue
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }
}
