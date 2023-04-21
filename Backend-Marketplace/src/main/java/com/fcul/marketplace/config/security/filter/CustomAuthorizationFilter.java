package com.fcul.marketplace.config.security.filter;



import com.auth0.jwt.interfaces.DecodedJWT;
import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    SecurityUtils securityUtils;

    public CustomAuthorizationFilter(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        try {
            String token = securityUtils.getToken(authorizationHeader);
            DecodedJWT decodedJWT = securityUtils.verifyToken(token);
            String username = decodedJWT.getSubject();
            String role = decodedJWT.getClaim("role").asString();
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = Arrays.asList(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (JWTTokenMissingException jwtTokenMissingException) {
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            response.setHeader("error", exception.getMessage());
            response.setStatus(FORBIDDEN.value());
        }

    }
}


