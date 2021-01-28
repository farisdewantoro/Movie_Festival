package movie.festival.service;

import movie.festival.exception.AppException;
import movie.festival.model.Role;
import movie.festival.model.RoleName;
import movie.festival.model.User;
import movie.festival.payload.ApiResponse;
import movie.festival.payload.JwtAuthenticationResponse;
import movie.festival.payload.SignInRequest;
import movie.festival.payload.SignUpRequest;
import movie.festival.repository.RoleRepository;
import movie.festival.repository.UserRepository;
import movie.festival.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    JwtTokenProvider tokenProvider;

    @Override
    public ResponseEntity<?> register(SignUpRequest signUpRequest) {
        if(userRepository.existsByUserName(signUpRequest.getUsername()) || userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Username/Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        UUID id = UUID.randomUUID();
        // Creating user's account
        User user = new User(id,signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/{id}")
                .buildAndExpand(result.getId()).toUri();

        logger.info(result.toString() + ", location :"+ location.toString());

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));

    }

    @Override
    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsernameOrEmail(),
                        signInRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
