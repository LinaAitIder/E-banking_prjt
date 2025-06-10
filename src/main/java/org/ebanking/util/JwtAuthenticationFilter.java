package org.ebanking.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if (token != null && jwtUtil.validateToken(token)) {
                Authentication authentication = createAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Vérification des rôles pour les endpoints admin
                if (request.getRequestURI().startsWith("/api/admin") &&
                        !authentication.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                    return;
                }
            }
        } catch (ExpiredJwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        } catch (Exception ex) {
            logger.error("Authentication error", ex);
        }

        filterChain.doFilter(request, response);
    }

//    private String extractToken(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
//            return header.substring(7);
//        }
//        return null;
//    }

    private String extractToken(HttpServletRequest request) {
        return jwtUtil.extractJwtFromRequest(request);
    }

    private Authentication createAuthentication(String token) {
        String username = jwtUtil.extractUsername(token);
        List<GrantedAuthority> authorities = jwtUtil.extractAuthorities(token);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );
    }


}
