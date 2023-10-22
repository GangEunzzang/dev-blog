package com.devblog.security.oauth;

import com.devblog.domain.entity.SocialType;
import com.devblog.domain.entity.User;
import com.devblog.domain.entity.UserRole;
import com.devblog.domain.repository.UserRepository;
import com.devblog.exception.CustomException;
import com.devblog.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //OAuth2 로그인 플랫폼 구분
        SocialType socialType = SocialType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new CustomException(ErrorCode.NOT_EXISTS_EMAIL_OAUTH2);
        }

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        //이미 가입된 경우
        if (user != null) {
            if (!user.getSocialType().equals(socialType)) {
                throw new CustomException(ErrorCode.ALREADY_EXISTS_USER);
            }
            user = updateUser(user, oAuth2UserInfo);
        }
        //가입되지 않은 경우
        else {
            user = registerUser(socialType, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
    }

    private User registerUser(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .socialId(oAuth2UserInfo.getSocialId())
                .socialType(socialType)
                .userRole(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(user.update(oAuth2UserInfo));
    }
}