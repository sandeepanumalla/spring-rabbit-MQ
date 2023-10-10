package com.example.rabbitmq;

import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class RabbitMqApplication {

    public static void main(String[] args) {
       ConfigurableApplicationContext applicationContext = SpringApplication.run(RabbitMqApplication.class, args);
        String applicationType = System.getProperty("spring.application.type");

        if(applicationType.isEmpty()) {
           System.out.println("error");
       }
       else if(applicationType.equals("producer")) {
            invokeProducer(applicationContext);
       } else if(applicationType.equals(("consumer"))) {
           invokeConsumer(applicationContext);
       }

    }

     public static void invokeProducer(ConfigurableApplicationContext applicationContext) {
        Producer producer = applicationContext.getBean(Producer.class);
        try {
            System.out.println("sending message...");
            producer.sendMessage();
        } catch (IOException e) {
            System.out.println("exception occurred !!" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void invokeConsumer(ConfigurableApplicationContext applicationContext) {
        Consumer consumer = applicationContext.getBean(Consumer.class);
        try {
            System.out.println("receiving message...");
            consumer.receiveMessage();
        } catch (IOException e) {
            System.out.println("exception occurred !!" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
