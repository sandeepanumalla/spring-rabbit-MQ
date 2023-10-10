package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Producer {


    private final Channel channel;

    @Autowired
    public Producer(Channel channel) {
        this.channel = channel;
    }

    public void sendMessage() throws IOException {
        channel.queueDeclare("my_queue", false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", "my_queue", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
    }
}
