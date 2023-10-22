package com.devblog.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER"), MANAGER("ROLE_MANAGER"), ADMIN("ROLE_ADMIN"), GUEST("ROLE_GUEST");

    private final String key;

    public static UserRole of(String code) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.getKey().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
