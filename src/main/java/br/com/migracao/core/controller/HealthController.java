package br.com.migracao.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public HealthResponse health() {
        return new HealthResponse("UP", "core-api", OffsetDateTime.now());
    }

    public record HealthResponse(String status, String service, OffsetDateTime timestamp) {
    }
}
