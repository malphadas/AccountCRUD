package com.malpha.acccrud.Config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> {
                                        auth.requestMatchers("/", "/login", "/register", "/user/**").permitAll();
                                        auth.requestMatchers("/admin").hasRole("ADMIN");
                                        auth.anyRequest().hasAnyRole("USER", "ADMIN");
                                })
                                .formLogin(login ->
                                        login.loginPage("/login")
                                        .loginProcessingUrl("/login")
                                        .defaultSuccessUrl("/success")
                                        .permitAll())
                                .logout(logout -> logout.logoutSuccessUrl("/login"))
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(withDefaults())
                                .build();
        }
        
}
