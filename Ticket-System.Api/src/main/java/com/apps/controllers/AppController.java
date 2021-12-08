package com.apps.controllers;

import com.apps.config.kafka.Message;
import com.apps.filter.JWTService;
import com.apps.request.ScheduleEmailRequest;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class AppController {

    @Autowired
    private JWTService jwtService;

    private final RestTemplate restTemplate;

    public AppController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/test")
    @ResponseBody
    public Object index() {
        var scheduleEmail = new ScheduleEmailRequest();
        scheduleEmail.setEmail("aupv96@gmail.com");
        scheduleEmail.setSubject("test email");
        scheduleEmail.setBody("Hello");
        var response = restTemplate.postForEntity("http://localhost:8081/api/v1/scheduleEmail",scheduleEmail,ScheduleEmailRequest.class);
        return response.getBody();
    }

    @GetMapping("/testCreateToken")
    @ResponseBody
    public String generateToken() throws JOSEException {
        return jwtService.generatorToken("");
    }

    @GetMapping("/verifyToken")
    @ResponseBody
    public Boolean verrifyToken(@RequestParam("token") String token) throws JOSEException, ParseException {
        return jwtService.verifyToken(token);
    }

//    @MessageMapping("/seat-map")
//    @SendTo("/topic/messages")
//    public OutputMessage send(Message message) throws Exception {
//        String time = new SimpleDateFormat("HH:mm").format(new Date());
//        return new OutputMessage(message.getFrom(), message.getText(), time);
//    }

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @PostMapping(value = "/api/send", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody Message message) {
        try {
            //Sending the message to kafka topic queue
            kafkaTemplate.send("test-websocket", message).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @SendTo("/topic/notification")
    public Message broadcastAllOrder(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }

    @SendTo("/topic/seat-map")
    public Message broadcastAllSeatMap(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }

//    @MessageMapping("/newUser")
//    @SendTo("/topic/group")
//    public Message addUser(@Payload Message message,
//                           SimpMessageHeaderAccessor headerAccessor) {
//        // Add user in web socket session
//        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", message.get());
//        return message;
//    }

}
