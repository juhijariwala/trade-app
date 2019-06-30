package com.currencyfair.tradepublisher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("trade-publisher/health")
public class HealthController {

    @GetMapping
    ResponseEntity healthCheck() {
        return ResponseEntity.ok("Service is up");
    }
}
