package com.october.back.config;

import com.october.back.security.oauth2.CustomAuthenticationFailureHandler;
import com.october.back.security.oauth2.CustomSuccessHandler;
import com.october.back.security.oauth2.service.CustomOAuth2UserService;
import com.october.back.util.jwt.JwtFilter;
import com.october.back.util.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        // todo : 추후에 프론트 주소에 맞게 변경 필요
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                        return configuration;
                    }
                }))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(
                        oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                                .successHandler(customSuccessHandler)
                                .failureHandler(customAuthenticationFailureHandler))  // OAuth2 로그인 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/**", "/public/**", "/resources/**", "/META-INF/resources/**")
                        .permitAll() // 정적 리소스 접근 허용
                        .requestMatchers("/", "/api/main", "/api/test", "/login/**", "/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll() // 인증 없이 접근할 수 있는 URL들
                        .anyRequest().authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 설정 (Stateless)
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/api/test")
                        .invalidateHttpSession(true).clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "Authorization"))  // 로그아웃 설정
                .build();
    }
}
