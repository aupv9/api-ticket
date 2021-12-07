package com.apps.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = "test-websocket",
            groupId = "real-time"
    )
    public void listen(ConsumerRecord<String, Message> record) {
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/chart", record.value());
    }
}
