package com.devblog.security.oauth.handler;

import com.devblog.security.jwt.JwtProvider;
import com.devblog.security.oauth.cookie.CookieAuthorizationRequestRepository;
import com.devblog.security.oauth.cookie.CookieUtils;
import com.devblog.security.oauth.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.devblog.security.oauth.cookie.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtProvider jwtProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // token 들어있음
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        System.out.println("redirectUri = " + redirectUri);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("잘못된 리다이렉트 경로입니다. 인증을 진행할 수 없습니다.");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        //JWT 생성
        UserResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenInfo.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        System.out.println("clientRedirectUri = " + clientRedirectUri);
        URI authorizedUri = URI.create(redirectUri);
        System.out.println("authorizedUri = " + authorizedUri);
        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort();
    }
}