package com.projectmanagement.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@WebFilter("/*")
public class JWTAuthenticationFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String header = httpRequest.getHeader(HEADER_STRING);

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.substring(TOKEN_PREFIX.length());

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Invalid or expired token.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
