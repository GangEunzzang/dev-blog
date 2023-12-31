package com.devblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 존재하지 않습니다."),
    EXISTS_EMAIL(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    NOT_EXISTS_USER(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    ALREADY_EXISTS_USER(HttpStatus.BAD_REQUEST, "이미 사용자가 존재합니다."),

    NOT_EXISTS_EMAIL_OAUTH2(HttpStatus.BAD_REQUEST, "이메일이 존재하지 않습니다. Oauth2 요청을 확인해주세요"),
    INVALID_TOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

}
