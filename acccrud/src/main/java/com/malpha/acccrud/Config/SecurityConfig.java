package com.malpha.acccrud.Config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(encoder());

                return provider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(auth -> {
                                        auth.requestMatchers("/", "/login", "/register", "/user/**","/swagger-ui/**", "/api-docs/**").permitAll();
                                        auth.requestMatchers("/admin").hasRole("ADMIN");
                                        auth.anyRequest().hasAnyRole("USER", "ADMIN");
                                })
                                .sessionManagement(session ->
                                         session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                         .sessionFixation().newSession())
                                .formLogin(login ->
                                        login.loginPage("/login")
                                        .loginProcessingUrl("/login")
                                        .defaultSuccessUrl("/user/s", true)
                                        .permitAll())
                                .logout(logout ->
                                          logout.logoutUrl("/logout")
                                        .logoutSuccessUrl("/")
                                        .invalidateHttpSession(true)
                                        .clearAuthentication(true)
                                        .deleteCookies("JSESSIONID"))
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(withDefaults())
                                .build();
        }
        
}