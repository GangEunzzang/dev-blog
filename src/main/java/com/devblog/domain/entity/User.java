package com.devblog.domain.entity;

import com.devblog.security.oauth.OAuth2UserInfo;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;

    public User update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.socialId = oAuth2UserInfo.getSocialId();

        return this;
    }
}
