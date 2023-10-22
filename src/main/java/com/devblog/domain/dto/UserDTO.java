package com.devblog.domain.dto;

import com.devblog.domain.entity.User;
import com.devblog.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Request {

        @NotBlank(message = "아이디를 입력해주세요")
        private String email;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, message = "닉네임이 너무 짧습니다.")
        private String nickName;

        @NotNull(message = "나이를 입력해주세요")
        @Range(min = 0, max = 150)
        private int age;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .name(nickName)
                    .userRole(UserRole.USER)
                    .build();
        }

    }
}
