package com.devblog.domain.dto;

import com.devblog.domain.entity.User;
import com.devblog.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
                message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
        private String password;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .nickName(nickName)
                    .age(age)
                    .password(password)
                    .userRole(UserRole.USER)
                    .build();
        }

    }
}
