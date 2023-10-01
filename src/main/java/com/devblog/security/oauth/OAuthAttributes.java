package com.devblog.security.oauth;

import com.devblog.domain.entity.SocialType;
import com.devblog.domain.entity.User;
import com.devblog.domain.entity.UserRole;
import com.devblog.exception.CustomException;
import com.devblog.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.management.relation.Role;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }

        throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickName(oauth2UserInfo.getNickname())
                .imageUrl(oauth2UserInfo.getImageUrl())
                .userRole(UserRole.USER)
                .build();
    }
}
