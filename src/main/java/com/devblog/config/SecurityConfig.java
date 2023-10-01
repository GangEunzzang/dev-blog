package com.devblog.config;

import com.devblog.domain.repository.UserRepository;
import com.devblog.security.jwt.JwtAuthenticationFilter;
import com.devblog.security.jwt.JwtProvider;
import com.devblog.security.oauth.CustomOAuth2UserService;
import com.devblog.security.oauth.handler.OAuth2LoginFailureHandler;
import com.devblog.security.oauth.handler.OAuth2LoginSuccessHandler;
import com.devblog.security.oauth.service.LoginService;
import com.devblog.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginService loginService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable() // FormLogin 사용 X
                .httpBasic().disable() // httpBasic 사용 X
                .csrf().disable() // csrf 보안 사용 X
                .headers().frameOptions().disable()
                .and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //== URL별 권한 관리 옵션 ==//
                .authorizeRequests()

                .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico").permitAll()
                .antMatchers("/sign-up").permitAll() // 회원가입 접근 가능
                .anyRequest().authenticated() // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
                .and()
                //== 소셜 로그인 설정 ==//
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

        return http.build();
    }

//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/login", "/signup", "/read/**", "**").permitAll()
//                .antMatchers("/register", "/modify/**").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(jwtAuthenticationProcessingFilter
//                        UsernamePasswordAuthenticationFilter.class);
//
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.csrf().disable()
//            .formLogin().disable()
//            .httpBasic().disable();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
            provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationFilter(jwtProvider, userRepository);
    }
}
