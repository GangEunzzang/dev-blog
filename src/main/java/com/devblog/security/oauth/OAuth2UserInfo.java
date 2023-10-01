package com.devblog.security.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    protected Map<String, Object> attributes;

    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract String getImageUrl();
}
