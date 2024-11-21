// SecurityConfig.java
package com.nikolas.leaflet.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nikolas.leaflet.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/","/update","/index", "/map/index", "/register", "/map/unidades","/dialogflow/webhook", "/css/**", "/js/**", "/images/**")
                .permitAll()  // Permite acceso a estas rutas sin autenticación
                .anyRequest().authenticated()  // Requiere autenticación para el resto
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/map/index", true)
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            .and()
            .csrf().disable();

        return http.build();
    }

}
