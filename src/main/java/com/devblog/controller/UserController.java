package com.devblog.controller;

import com.devblog.domain.dto.UserDTO;
import com.devblog.security.oauth.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final LoginService loginService;

    @GetMapping("/auth/login")
    public void login(@AuthenticationPrincipal OAuth2User oAuth2User) {
        loginService.loadUserByUsername(oAuth2User.getName());
    }

    @GetMapping("/signup")
    public String join() {
        return "signup";
    }

}

