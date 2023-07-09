package com.devblog.exception;

import lombok.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
