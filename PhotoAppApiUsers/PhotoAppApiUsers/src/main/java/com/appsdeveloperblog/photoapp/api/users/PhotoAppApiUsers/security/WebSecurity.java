package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final Environment environment;

    public WebSecurity(Environment environment) {

        this.environment = environment;

    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        http.getSharedObject()

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/users", HttpMethod.POST.toString()))
                        .access(new WebExpressionAuthorizationManager(
                                "hasIpAddress('"+ environment.getProperty("domain.default.ipAddress") +"')"
                        ))
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
//                .authenticationManager(authenticationManager)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
