package com.example.crappostsbackend.filter;

import com.example.crappostsbackend.service.AppUserService;
import com.example.crappostsbackend.util.JsonWebTokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

   private final AppUserService appUserService;
    private final JsonWebTokenUtility jsonWebTokenUtility;

    @Autowired
    public JwtRequestFilter(AppUserService appUserService, JsonWebTokenUtility jsonWebTokenUtility) {
        this.appUserService = appUserService;
        this.jsonWebTokenUtility = jsonWebTokenUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        String prefix = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {
            jwt = authorizationHeader.substring(prefix.length());
            username = jsonWebTokenUtility.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserService.loadUserByUsername(username);
            if (jsonWebTokenUtility.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
