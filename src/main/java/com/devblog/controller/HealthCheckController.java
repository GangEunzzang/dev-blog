package com.devblog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }
}
