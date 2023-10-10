package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/queues")
public class controller {

    private final Channel channel;
    private final NotificationProducer notificationProducer;

    private final NotificationConsumer notificationConsumer;

    @Autowired
    public controller(Channel channel, NotificationProducer notificationProducer, NotificationConsumer notificationConsumer) {
        this.channel = channel;
        this.notificationProducer = notificationProducer;
        this.notificationConsumer = notificationConsumer;
    }

    public final String QUEUE_NAME = "my_queue";

    @GetMapping("/send/{message}")
    public NotificationDTO producer(@PathVariable(name = "message") String message) throws IOException {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .title("anything")
                .message(message)
                .build();
        notificationProducer.sendNotification(notificationDTO);
        System.out.println("notification sent.");
        return null;
    }

    @GetMapping("/get")
    public String consumer() throws IOException {
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);
        AtomicReference<String> message = new AtomicReference<>();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
             message.set(new String(delivery.getBody(), "UTF-8"));
            System.out.println(" [x] Received '" + message + "'");
            // Manually acknowledge the message after processing
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        return message.get();
    }
}
