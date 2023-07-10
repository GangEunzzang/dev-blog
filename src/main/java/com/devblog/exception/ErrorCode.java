package com.devblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /** 404 NOT_FOUND */
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

}
