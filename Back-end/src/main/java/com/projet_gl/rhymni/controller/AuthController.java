package com.projet_gl.rhymni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.ResponseUserInfo;
import com.projet_gl.rhymni.entity.User;
import com.projet_gl.rhymni.security.AuthRequest;
import com.projet_gl.rhymni.security.AuthResponse;
import com.projet_gl.rhymni.security.JwtTokenUtil;
import com.projet_gl.rhymni.service.StudentService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(), request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getMailAddress(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/getUserInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseUserInfo getUserInfo(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtUtil.parseClaims(token);
        if ((claims.get("role").toString()).equals("ROLE_TEAM_MEMBER")) {
            return ResponseUserInfo.builder()
                    .userId((Integer) claims.get("id"))
                    .mailAddress(claims.getSubject())
                    .role(claims.get("role").toString())
                    .idTeam(studentService.getStudentById((Integer) claims.get("id")).getIdTeam())
                    .build();
        } else {
            return ResponseUserInfo.builder()
                    .userId((Integer) claims.get("id"))
                    .mailAddress(claims.getSubject())
                    .role(claims.get("role").toString())
                    .build();
        }
    }
}
