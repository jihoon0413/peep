package com.example.peep.config;

import com.example.peep.config.jwt.JwtAuthenticationFilter;
import com.example.peep.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login","/loginForm","/joinForm").permitAll()
                        .requestMatchers("/students/new").permitAll()
                        .requestMatchers("/schools/getList").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/sendCode").permitAll()
                        .requestMatchers("/auth/verifyCode").permitAll()
                        .requestMatchers("/follow/getFollowingList").permitAll()
                        .requestMatchers("/follow/getFollowerList").permitAll()
                        .requestMatchers("/hashtag/getHashList").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
