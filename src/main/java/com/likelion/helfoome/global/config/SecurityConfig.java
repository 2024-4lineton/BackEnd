package com.likelion.helfoome.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    http.csrf(AbstractHttpConfigurer::disable)
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
                    .requestMatchers("/",
                        "/swagger-ui/**",
                        "/api/users/**",
                        "/api/shop/**",
                        "/api/product/**")
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
                    .successHandler(customSuccessHandler))
        .cors(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용
    configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
    configuration.addAllowedHeader("*"); // 모든 헤더 허용
    configuration.setAllowCredentials(true); // 자격 증명 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
