package com.devblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /** 404 NOT_FOUND */
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 존재하지 않습니다."),
    EXISTS_EMAIL(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    NOT_EXISTS_USER(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),

    INVALID_TOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다");

    private final HttpStatus httpStatus;
    private final String errorMessage;

}
