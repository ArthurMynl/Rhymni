package com.projet_gl.rhymni.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projet_gl.rhymni.entity.Role;
import com.projet_gl.rhymni.entity.User;

import io.jsonwebtoken.Claims;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
        } else {
            String token = getAccessToken(request);
            if (!jwtUtil.validateAccessToken(token)) {
                filterChain.doFilter(request, response);
            } else {
                setAuthenticationContext(token, request);
                filterChain.doFilter(request, response);
            }
        }
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1].trim();
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        Claims claims = jwtUtil.parseClaims(token);
        String role = (String) claims.get("role");
        role = role.replace("[", "").replace("]", "");
        if (role.equals("ROLE_TEACHER")) {
            userDetails.setRole(Role.ROLE_TEACHER);
        } else if (role.equals("ROLE_PLANNING_ASSISTANT")) {
            userDetails.setRole(Role.ROLE_PLANNING_ASSISTANT);
        } else if (role.equals("ROLE_STUDENT")) {
            userDetails.setRole(Role.ROLE_STUDENT);
        } else if (role.equals("ROLE_TEAM_MEMBER")) {
            userDetails.setRole(Role.ROLE_TEAM_MEMBER);
        } else if (role.equals("ROLE_OPTION_LEADER")) {
            userDetails.setRole(Role.ROLE_OPTION_LEADER);
        } else if (role.equals("ROLE_JURY_MEMBER")) {
            userDetails.setRole(Role.ROLE_JURY_MEMBER);
        } else {
            throw new IllegalArgumentException("Unknown role: " + role);
        }
        String[] jwtSubject = jwtUtil.getSubject(token).split(",");
        userDetails.setMailAddress(jwtSubject[0]);
        return userDetails;
    }
}
