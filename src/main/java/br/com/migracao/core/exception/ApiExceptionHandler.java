package br.com.migracao.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> tratarRegraNegocio(
            BusinessRuleException exception,
            HttpServletRequest request
    ) {
        return construirResposta(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> tratarRegistroNaoEncontrado(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return construirResposta(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String mensagem =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(error ->
                                error.getDefaultMessage() != null
                                        ? error.getDefaultMessage()
                                        : "Dados inválidos."
                        )
                        .orElse("Dados inválidos.");

        return construirResposta(
                HttpStatus.BAD_REQUEST,
                mensagem,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return construirResposta(
                HttpStatus.BAD_REQUEST,
                "Dados informados são inválidos.",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiError> construirResposta(
            HttpStatus status,
            String mensagem,
            String path
    ) {
        ApiError erro =
                new ApiError(
                        LocalDateTime.now().withNano(0),
                        status.value(),
                        status.getReasonPhrase(),
                        mensagem,
                        path
                );

        return ResponseEntity
                .status(status)
                .body(erro);
    }

    public record ApiError(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
    }
}