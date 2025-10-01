package com.example.order_service.controller;

import com.example.order_service.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Operation(summary = "Get user by order ID", description = "Fetches user details for a given order ID from user-service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/user")
    public UserDTO getOrderUser(@PathVariable Long id) {
        String url = userServiceUrl + "/users/" + id;
        logger.info("Calling user-service at URL: {}", url);
        try {
            UserDTO response = restTemplate.getForObject(url, UserDTO.class);
            if (response == null) {
                logger.warn("No user found for ID: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            logger.info("Response from user-service: id={}, name={}, email={}",
                    response.getId(), response.getName(), response.getEmail());
            return response;
        } catch (Exception e) {
            logger.error("Error calling user-service: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch user from user-service", e);
        }
    }
}