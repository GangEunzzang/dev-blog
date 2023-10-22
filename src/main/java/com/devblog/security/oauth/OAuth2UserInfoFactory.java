package com.devblog.security.oauth;

import com.devblog.domain.entity.SocialType;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
        switch (socialType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);

            default: throw new IllegalArgumentException("잘못된 로그인 요청입니다.");
        }
    }
}