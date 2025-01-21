package cibernarium.virtualPetBackEnd.authentication;


import cibernarium.virtualPetBackEnd.jwt.JwtService;
import cibernarium.virtualPetBackEnd.user.Role;
import cibernarium.virtualPetBackEnd.user.User;
import cibernarium.virtualPetBackEnd.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Este servicio se encarga de la autenticación del usuario comparando la request con lo existente en la DB

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Fetch the user details
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        // Generate the JWT token with user_id
        String token = jwtService.getToken(user, user.getId());

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        // Create a new user
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        // Save the user to the database
        userRepository.save(user);

        // Generate the JWT token with user_id
        String token = jwtService.getToken(user, user.getId());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}