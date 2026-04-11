package com.platform.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles error pages properly to avoid "Whitelabel Error Page"
 */
@RestController
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int statusCode = status != null ? (int) status : 500;
        String errorMessage = message != null ? message.toString() : "An error occurred";

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", statusCode);
        response.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        response.put("message", errorMessage);
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }
}
