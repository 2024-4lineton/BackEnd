package com.likelion.helfoome.global.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.likelion.helfoome.domain.user.service.UserService;
import com.likelion.helfoome.global.auth.handler.CustomSuccessHandler;
import com.likelion.helfoome.global.auth.jwt.JwtFilter;
import com.likelion.helfoome.global.auth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, UserService userService)
      throws Exception {
    http.cors(
            corsCustomizer ->
                corsCustomizer.configurationSource(
                    request -> {
                      CorsConfiguration configuration = new CorsConfiguration();

                      configuration.setAllowedOrigins(
                          Arrays.asList(
                              "http://localhost:3000",
                              "http://localhost:8080",
                              "https://metalog.store"));
                      configuration.setAllowedMethods(Collections.singletonList("*"));
                      configuration.setAllowCredentials(true);
                      configuration.setAllowedHeaders(Collections.singletonList("*"));

                      configuration.setMaxAge(3600L);

                      configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

                      return configuration;
                    }))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        // Bearer 방식을 사용할건데 스프링 시큐리티는 httpBasic 이 잡혀있음
        .httpBasic(AbstractHttpConfigurer::disable)
        // Session 방식 사용 안 함
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Requests 경로별 요청 인가
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers(
                        "/",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/users/**",
                        "/api/shop/**",
                        "/api/product/**",
                        "/api/distance",
                        "/api/order/**")
                    .permitAll()
                    .requestMatchers("/api/v1/user/*")
                    .hasRole("USER || SELLER")
                    .requestMatchers("/api/v1/admin/*")
                    .hasRole("ADMIN")
                    // 열어준 요청 외에는 모두 권한 필요
                    .anyRequest()
                    .authenticated())
        // Jwt 필터 추가
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(
            (oauth2) ->
                oauth2
                    .userInfoEndpoint(
                        (userInfoEndpointConfig) ->
                            userInfoEndpointConfig.userService(customOAuth2UserService))
                    .successHandler(customSuccessHandler));

    return http.build();
  }
}
