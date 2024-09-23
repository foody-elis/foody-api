package com.example.foody.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    // String message or Map<String, String> message
    private Object message;
    private String path;
}
