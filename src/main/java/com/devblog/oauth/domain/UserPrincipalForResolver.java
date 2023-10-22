package com.devblog.oauth.domain;


import com.devblog.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipalForResolver {
    private String userEmail;
    private String userNickname;
    private UserRole authorities;
}
