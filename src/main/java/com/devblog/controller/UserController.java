package com.devblog.controller;

import com.devblog.domain.dto.UserDTO;
import com.devblog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String join() {
        return "signup";
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public String join(@Valid UserDTO.Request request) {
        return userService.signUp(request);
    }
}
