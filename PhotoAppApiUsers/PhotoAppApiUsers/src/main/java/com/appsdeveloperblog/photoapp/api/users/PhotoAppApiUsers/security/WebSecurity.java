package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.security;

import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.security.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final Environment environment;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationFilter authenticationFilter;

    public WebSecurity(Environment environment, AuthenticationManager authenticationManager,
                       AuthenticationFilter authenticationFilter) {

        this.environment = environment;
        this.authenticationManager = authenticationManager;
        this.authenticationFilter = authenticationFilter;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        authenticationFilter.setFilterProcessesUrl("/users/login");

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/users", HttpMethod.POST.toString())).access(
                                new WebExpressionAuthorizationManager("hasIpAddress('"+ environment.getProperty("domain.default.ipAddress") +"')"))
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
