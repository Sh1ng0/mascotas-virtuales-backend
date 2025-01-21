package cibernarium.virtualPetBackEnd.config;

import cibernarium.virtualPetBackEnd.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig{

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        Logger logger = LoggerFactory.getLogger(getClass());  // Logger para este método
//
//        // Log de inicio de configuración
//        logger.info("Iniciando configuración de seguridad DESDE SECURITY CONFIG...SecurityFilterChain");
//
//        return http
//                .csrf(csrf -> {
//                    logger.info("Deshabilitando CSRF");
//                    csrf.disable();
//                })
//                .authorizeHttpRequests(authRequest -> {
//                    logger.info("Configurando autorización de rutas...");
//                    authRequest
//                            .requestMatchers("/auth/**").permitAll()
//                            .requestMatchers("/pet/**").authenticated()
//                            .anyRequest().authenticated();
//                })
//                .sessionManagement(sessionManager -> {
//                    logger.info("Configurando la gestión de sesiones (STATLESS).");
//                    sessionManager
//                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                })
//                .authenticationProvider(authProvider -> {
//                    logger.info("Configurando proveedor de autenticación personalizado.");
//                    // Aquí se puede agregar más información si es necesario.
//                })
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .cors(Customizer.withDefaults())
//                .build();
//
//        // Log de finalización de configuración
//        logger.info("Configuración de seguridad finalizada.");
//    }


// SIN LOGER:

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        Logger logger = LoggerFactory.getLogger(SecurityConfig.class);


        logger.info("Iniciando configuración de seguridad...");
        return http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/pet/**").hasAuthority("USER")
//                                .requestMatchers("/pet/**").authenticated() // Permitir rutas de autenticación
                                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No guardar estado en sesiones
                )
                .authenticationProvider(authProvider) // Proveedor de autenticación personalizado
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Filtro JWT personalizado
                .cors(Customizer.withDefaults()) // Configuración por defecto de CORS
                .build();
    }





}
