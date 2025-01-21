package cibernarium.virtualPetBackEnd.config;


import cibernarium.virtualPetBackEnd.user.Role;
import cibernarium.virtualPetBackEnd.user.User;
import cibernarium.virtualPetBackEnd.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor

    public class ApplicationConfig {

        private final UserRepository userRepository;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
        {
            return config.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider()
        {
            DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailService() {
            return username -> userRepository.findByUsername(username)
                    .orElseThrow(()-> new UsernameNotFoundException("User not fournd"));
        }

    }


//    @Bean
//    public UserDetailsService userDetailService() {
//        return username -> {
//            // Recuperar el usuario de la base de datos
//            User user = userRepository.findByUsername(username)
//                    .orElseGet(() -> {
//                        // Si no se encuentra el usuario, creamos uno por defecto
//                        User defaultUser = new User();
//                        defaultUser.setUsername("defaultUser");  // Nombre por defecto
//                        defaultUser.setPassword(passwordEncoder().encode("defaultPassword"));  // Contraseña por defecto
//                        defaultUser.setRoles(List.of(Role.USER));  // Asignamos un rol por defecto (USER)
//
//                        return defaultUser;
//                    });
//
//            return new org.springframework.security.core.userdetails.User(
//                    user.getUsername(),
//                    user.getPassword(),
//                    user.getAuthorities()  // Usar los roles como Authorities
//            );
//        };


//    @Bean
//    public UserDetailsService userDetailService() {
//        return username -> {
//            // Recuperar el usuario de la base de datos
//            User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//            // Si el usuario no tiene roles asignados, asigna un rol por defecto (por ejemplo, "USER")
//            if (user.getRoles() == null || user.getRoles().isEmpty()) {
//                user.setRoles(List.of(Role.USER));  // Asignar un rol por defecto
//            }
//
//            return new org.springframework.security.core.userdetails.User(
//                    user.getUsername(),
//                    user.getPassword(),
//                    user.getAuthorities()  // Usar los roles como Authorities
//            );
//        };
//    }





