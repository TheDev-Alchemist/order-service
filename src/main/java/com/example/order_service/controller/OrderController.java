package com.example.orderservice.controller;

import com.example.orderservice.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate = new RestTemplate();

    // URL artık property’den alınıyor, profile’a göre değişecek
    @Value("${user.service.url}")
    private String userServiceUrl;

    @GetMapping("/{id}/user")
    public UserDTO getOrderUser(@PathVariable Long id) {
        String url = userServiceUrl + "/users/" + id;
        return restTemplate.getForObject(url, UserDTO.class);
    }
}
