package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeRequests(auth -> auth
                        .requestMatchers("/fail").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/contracts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/contracts/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/contracts").hasRole("CLIENT")
                        .requestMatchers("/h2-console").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                //для h2-console
                .headers(headers -> headers.frameOptions().disable())
                .formLogin(fm -> {
                    fm.failureForwardUrl("/fail");
                })
                .build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}