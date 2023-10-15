package com.sulsul.api.config;

import com.sulsul.api.config.jwt.JwtAuthenticationFilter;
import com.sulsul.api.config.oauth.CustomOAuth2UserService;
import com.sulsul.api.config.oauth.OAuth2AuthenticationFailureHandler;
import com.sulsul.api.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.sulsul.api.config.security.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_URL = {
            "/api-docs",
            "/v3/api-docs/**",
            "/swagger-*/**",
            "/webjars/**"
    };
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint entryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final String[] GET_PERMIT_API_URL = {
            "/",
            "/refresh",
    };

    private final String[] POST_PERMIT_API_URL = {
            "/refresh",
            "/users/auth/token/kakao"
    };

    private final LogoutHandler logoutService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(c -> c.disable())
                .formLogin(c -> c.disable())
                .httpBasic(c -> c.disable())
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeRequests(c -> c.requestMatchers(SWAGGER_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PERMIT_API_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, POST_PERMIT_API_URL).permitAll()
                        .anyRequest().authenticated()
                )

                .logout(c -> c.logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))

                .exceptionHandling(c -> c.authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))

                .oauth2Login(login -> login.successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                        .userInfoEndpoint(c -> c.userService(customOAuth2UserService)))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of("*"));

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}