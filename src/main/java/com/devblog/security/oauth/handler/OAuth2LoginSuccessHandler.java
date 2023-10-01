package com.devblog.security.oauth.handler;

import com.devblog.domain.repository.UserRepository;
import com.devblog.security.jwt.JwtProvider;
import com.devblog.security.oauth.CustomOAuth2User;
import com.devblog.security.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("OAuth2 Login 성공!");
        loginSuccess(response, (CustomOAuth2User) authentication.getPrincipal()); // 로그인에 성공한 경우 access, refresh 토큰 생성
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {
        String accessToken = jwtProvider.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtProvider.createRefreshToken();
        response.addHeader(jwtProvider.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtProvider.getRefreshHeader(), "Bearer " + refreshToken);

        jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtProvider.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
