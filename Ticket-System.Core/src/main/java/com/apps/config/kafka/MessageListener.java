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
        switch (record.key()){
            case "order-chart":
                System.out.println("get key == order-chart");
                template.convertAndSend("/topic/notification", record.value());
                break;
            case "seat-map":
                System.out.println("get key == seat-map");
                template.convertAndSend("/topic/notification", record.value());
            default:
                break;
        }
    }
}
