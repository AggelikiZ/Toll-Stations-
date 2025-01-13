package com.payway.utils;

import com.payway.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.payway.services.jwtBlackListService;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final jwtBlackListService jwtBlacklistService;

    public AuthenticationInterceptor(JwtTokenUtil jwtTokenUtil, jwtBlackListService jwtBlacklistService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnauthorizedException {
        String token = request.getHeader("X-OBSERVATORY-AUTH");
        String method = request.getMethod();

        // Allow CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        System.out.println("Token received: " + token);
        System.out.println("Request URI: " + request.getRequestURI());

        if (token == null || jwtBlacklistService.isBlacklisted(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        try {
            Claims claims = jwtTokenUtil.validateToken(token);
            String role = claims.get("role", String.class);

            if ((request.getRequestURI().startsWith("/api/payments") && !"operator".equals(role))
                    || (request.getRequestURI().startsWith("/api/admin") && !"admin".equals(role))) {
                throw new UnauthorizedException("Forbidden: Insufficient permissions");
            }
            return true;

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}



