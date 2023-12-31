package com.projet_gl.rhymni.security;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.projet_gl.rhymni.entity.PlanningAssistant;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.repository.PlanningAssistantRepository;
import com.projet_gl.rhymni.repository.StudentRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;

@Configuration
public class ApplicationSecurity {

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    UserDetailsService userDetailsService(TeacherRepository teacherRepository,
                                                  StudentRepository studentRepository,
                                                  PlanningAssistantRepository planningAssistantRepository) {
        return username -> {
            Optional<Student> student = studentRepository.findByMailAddress(username);
            if (student.isPresent()) {
                return student.get();
            }

            Optional<Teacher> teacher = teacherRepository.findByMailAddress(username);
            if (teacher.isPresent()) {
                return teacher.get();
            }

            Optional<PlanningAssistant> planningAssistant = planningAssistantRepository.findByMailAddress(username);
            if (planningAssistant.isPresent()) {
                return planningAssistant.get();
            }

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http.csrf().disable();
        http.cors();
        http.httpBasic();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                // .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()));
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
