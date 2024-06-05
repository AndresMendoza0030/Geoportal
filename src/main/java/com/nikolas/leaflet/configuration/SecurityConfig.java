package com.nikolas.leaflet.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Seguridad CSRF deshabilitada
            .authorizeRequests()
                .anyRequest().permitAll()  // Permitir todos los accesos
            .and()
            .formLogin()
                .disable()  // Deshabilitar el formulario de login
            .httpBasic()
                .disable();  // Deshabilitar autenticación básica
    }
}
