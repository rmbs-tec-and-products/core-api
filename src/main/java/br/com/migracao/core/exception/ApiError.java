package br.com.migracao.core.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(

        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        Map<String, String> fields
) {
}