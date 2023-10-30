package com.assignment.diffrenz.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    //Authentication
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN", "USER").build();

        UserDetails user = User.withUsername("user").password(encoder.encode("user")).roles("USER").build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    //Authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> {
                    //auth.requestMatchers("/api").permitAll();
                    auth.requestMatchers("/api/user/**").hasRole("USER");
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .httpBasic(withDefaults()).formLogin(log -> log.defaultSuccessUrl("/api"))
                .exceptionHandling(ex -> ex.accessDeniedPage("/api/access-denied"))
                .sessionManagement(session ->
                        session .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .invalidSessionUrl("/login")
                                .sessionFixation().none()
                                .sessionAuthenticationErrorUrl("/login")
                                .maximumSessions(2)
                                .maxSessionsPreventsLogin(true)
                                .expiredUrl("/login?invalid-session=true")
                )
                .logout(withDefaults())
        ;


        return http.build();

//                        ex.accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            response.setContentType("text/plain");
//                            response.getWriter().write("Access Denied: You do not have the necessary role to access this resource.");
//                        }))


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
