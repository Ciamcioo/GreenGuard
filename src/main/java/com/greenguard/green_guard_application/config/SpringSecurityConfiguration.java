package com.greenguard.green_guard_application.config;

import com.greenguard.green_guard_application.security.AuthEntryPointImpl;
import com.greenguard.green_guard_application.security.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConfiguration {

    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointImpl authEntryPointImpl;

    public SpringSecurityConfiguration(AuthTokenFilter authTokenFilter, AuthEntryPointImpl authEntryPointImpl) {
        this.authTokenFilter = authTokenFilter;
        this.authEntryPointImpl = authEntryPointImpl;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.cors(AbstractHttpConfigurer::disable)
                           .csrf(AbstractHttpConfigurer::disable)
                           .formLogin(AbstractHttpConfigurer::disable)
                           .httpBasic(AbstractHttpConfigurer::disable)
                           .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                           .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                           .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPointImpl))
                           .authorizeHttpRequests(auth -> auth
                               .requestMatchers("/api/auth/login",
                                                "api/auth/signup",
                                                "/h2-console/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html",
                                                "/v3/api-docs/**").permitAll()
                               .anyRequest().authenticated()
                           )
                           .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                           .build();
    }
}
