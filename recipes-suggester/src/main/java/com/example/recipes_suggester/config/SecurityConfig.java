package com.example.recipes_suggester.config;

import com.example.recipes_suggester.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").permitAll() // Allow access to Swagger
                .antMatchers("/api/users/register", "/api/users/login", "/api/users/current").permitAll() // Allow access to registration and login endpoints
                .antMatchers("/api/**").authenticated() // Protect all other API endpoints
                .and()
                .formLogin()
                .loginProcessingUrl("/api/users/login") // Set the login processing URL
                .usernameParameter("username") // Set the username parameter
                .passwordParameter("password") // Set the password parameter
                .defaultSuccessUrl("/api/users/current", true) // Redirect to /current on successful login
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable(); // Disable CSRF for simplicity, but consider enabling it in a real application

        // Session management
        http.sessionManagement()
                .maximumSessions(1) // Allow only one session per user
                .maxSessionsPreventsLogin(true); // Prevents user from logging in if they already have an active session
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
