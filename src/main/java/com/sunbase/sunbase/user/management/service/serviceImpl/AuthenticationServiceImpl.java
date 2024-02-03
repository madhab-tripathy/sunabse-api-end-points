package com.sunbase.sunbase.user.management.service.serviceImpl;

import com.sunbase.sunbase.user.management.Config.JwtService;
import com.sunbase.sunbase.user.management.dto.AuthRequest;
import com.sunbase.sunbase.user.management.dto.AuthResponse;
import com.sunbase.sunbase.user.management.dto.RegistrationRequest;
import com.sunbase.sunbase.user.management.entity.User;
import com.sunbase.sunbase.user.management.enums.Role;
import com.sunbase.sunbase.user.management.repository.UserRepository;
import com.sunbase.sunbase.user.management.service.AuthenticationService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * @param request user registration with email and password
     * @return authentication response with a JWT token
     */
    @Override
    public AuthResponse register(RegistrationRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * @param request user login with email and password
     * @return authentication response with a JWT token
     */
    @Override
    public AuthResponse authenticate(AuthRequest request) {
//        authenticate the user with correct credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
//        here we can handle exceptions but in this case the user will always be a valid user.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
