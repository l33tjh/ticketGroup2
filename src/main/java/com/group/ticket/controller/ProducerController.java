package com.group.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProducerController {
    private static final String EXCHANGE_NAME = "ticket.exchange";

    @Autowired
    RabbitTemplate rabbitTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/ticket/queue")
    public Map<String, String> Publish(@RequestBody Map<String, Object> requestData) {
        try {
            List<String> selectedSeats = (List<String>) requestData.get("seats");
            String sid = (String) requestData.get("sid");
            String email = (String) requestData.get("email");

            Map<String, Object> messageData = new HashMap<>();
            messageData.put("selectedSeats", selectedSeats);
            messageData.put("sid", sid);
            messageData.put("email", email);

            String json = objectMapper.writeValueAsString(messageData);
            rabbitTemplate.convertAndSend("ticket.queue", json);

            Map<String, String> response = new HashMap<>();
            return response;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "오류발생");
            return response;
        }
    }
}