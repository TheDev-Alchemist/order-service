package com.example.orderservice.controller;

import com.example.orderservice.model.UserDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/{id}/user")
    public UserDTO getOrderUser(@PathVariable Long id) {
        // Docker içinde container name ile erişim: user-service
        String url = "http://user-service:8081/users/" + id;
        return restTemplate.getForObject(url, UserDTO.class);
    }
}
