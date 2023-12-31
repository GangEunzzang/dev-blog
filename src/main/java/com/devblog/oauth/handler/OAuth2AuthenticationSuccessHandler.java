package com.devblog.oauth.handler;


import com.devblog.config.AppProperties;
import com.devblog.domain.entity.SocialType;
import com.devblog.domain.entity.UserRefreshToken;
import com.devblog.domain.entity.UserRole;
import com.devblog.domain.repository.UserRefreshTokenRepository;
import com.devblog.oauth.info.OAuth2UserInfo;
import com.devblog.oauth.info.OAuth2UserInfoFactory;
import com.devblog.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.devblog.oauth.token.AuthToken;
import com.devblog.oauth.token.AuthTokenProvider;
import com.devblog.oauth.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static com.devblog.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.*;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;


    //oauth2 인증이 성공적으로 이뤄졌을 때 실행됨
    //token을 포함한 uri를 생성 후 인증요청 쿠키를 비워주고 redirect 함
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    //token을 생성하고 이를 포함한 프론트엔드의 URI를 생성한다.
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("잘못된 리다이렉트 경로입니다. 인증을 진행할 수 없습니다.");
        }

        String targetUrl = redirectUri.orElse("/");

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        SocialType socialType = SocialType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        UserRole roleType = hasAuthority(authorities);

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userInfo.getEmail(),
                userInfo.getSocialId(),
                roleType.getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        // refresh 토큰 설정
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // DB 저장
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByEmail(userInfo.getEmail());
        if (userRefreshToken != null) {
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        } else {
            userRefreshToken = new UserRefreshToken(userInfo.getEmail(), refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        int cookieMaxAgeForAccess = (int) appProperties.getAuth().getTokenExpiry() / 1000;

        /*
        Access Token 저장
         */
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookieForAccess(response, ACCESS_TOKEN, accessToken.getToken(), cookieMaxAgeForAccess);

        /*
        Refresh Token 저장
         */
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    //인증정보 요청 내역에서 쿠키를 삭제
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private UserRole hasAuthority(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return UserRole.USER;
        }


        for (GrantedAuthority grantedAuthority : authorities) {
            if (UserRole.MANAGER.getKey().equals(grantedAuthority.getAuthority())) {
                return UserRole.MANAGER;
            }

            if (UserRole.ADMIN.getKey().equals(grantedAuthority.getAuthority())) {
                return UserRole.ADMIN;
            }
        }

        return UserRole.USER;
    }

    //application.oauth.yml을 통해서 등록해놓은 Redirect uri가 맞는지 확인한다.
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
