package br.com.migracao.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/check")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> check() {
        return Map.of(
            "status", "OK",
            "message", "Endpoint acessível somente por ADMIN"
        );
    }
}
