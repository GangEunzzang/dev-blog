package com.devblog.service;


import com.devblog.domain.dto.UserDTO;
import com.devblog.domain.entity.User;
import com.devblog.domain.repository.UserRepository;
import com.devblog.exception.CustomException;
import com.devblog.exception.ErrorCode;
import com.devblog.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String signUp(UserDTO.Request userRequestDTO) {

        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EXISTS_EMAIL);
        }

        User user = userRequestDTO.toEntity();
        user.encryptPassword(passwordEncoder);
        userRepository.save(user);

        return jwtTokenProvider.createToken(user.getEmail(), user.getUserRole().name());

    }

}
