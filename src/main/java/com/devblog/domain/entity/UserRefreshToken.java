package com.devblog.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRefreshToken {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenNumber;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 256, nullable = false)
    private String refreshToken;

    public UserRefreshToken(String userEmail, String refreshToken) {
        this.email = userEmail;
        this.refreshToken = refreshToken;
    }
}
