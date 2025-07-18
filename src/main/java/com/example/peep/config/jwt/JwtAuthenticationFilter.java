package com.example.peep.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest servletRequest, @NotNull HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        if(isAccessToken(servletRequest)) {
            processAccessToken(servletRequest, servletResponse);
        } else if(isRefreshToken(servletRequest)) {
            processRefreshToken(servletRequest, servletResponse);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void processAccessToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String token = resolveAccess(servletRequest);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAccessAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void processRefreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String token = resolveRefresh(servletRequest);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getRefreshAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    }

    private boolean isAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization") != null;
    }

    private boolean isRefreshToken(HttpServletRequest request) {
        return request.getHeader("refresh-token") != null;
    }

    private String resolveAccess(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveRefresh(HttpServletRequest request) {
        String bearerToken = request.getHeader("refresh-token");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
