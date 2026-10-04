package br.com.migracao.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException exception
    ) {
        ApiError error =
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity
                .status(
                        HttpStatus.NOT_FOUND
                )
                .body(
                        error
                );
    }

    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<ApiError> handleResourceInUse(
            ResourceInUseException exception
    ) {
        ApiError error =
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                )
                .body(
                        error
                );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(
            BusinessRuleException exception
    ) {
        ApiError error =
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        error
                );
    }

    @ExceptionHandler(
            MaxUploadSizeExceededException.class
    )
    public ResponseEntity<ApiError> handleMaxUploadSize(
            MaxUploadSizeExceededException exception
    ) {
        ApiError error =
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.PAYLOAD_TOO_LARGE.value(),
                        HttpStatus.PAYLOAD_TOO_LARGE
                                .getReasonPhrase(),
                        "O arquivo deve possuir no máximo 20 MB.",
                        null
                );

        return ResponseEntity
                .status(
                        HttpStatus.PAYLOAD_TOO_LARGE
                )
                .body(
                        error
                );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> fields =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fields.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiError error =
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Erro de validação",
                        fields
                );

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        error
                );
    }
}