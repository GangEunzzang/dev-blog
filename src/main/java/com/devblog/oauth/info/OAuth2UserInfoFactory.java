package com.devblog.oauth.info;

import com.devblog.domain.entity.SocialType;
import com.devblog.oauth.info.impl.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes){
        switch (socialType){
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type");
        }
    }
}
