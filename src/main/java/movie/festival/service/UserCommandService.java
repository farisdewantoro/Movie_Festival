package movie.festival.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie.festival.command.CreateUserCommand;
import movie.festival.payload.ApiResponse;
import movie.festival.payload.SignUpRequest;
import movie.festival.payload.UserCreatedResponse;
import movie.festival.repository.UserRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class UserCommandService {

    private final CommandGateway commandGateway;
    private final UserRepository userRepository;

    public ResponseEntity<?> createUser(SignUpRequest signUpRequest) {
        log.debug("Handling a createUser - UserCommandService {}", signUpRequest.toString());

        if(userRepository.existsByUserName(signUpRequest.getUsername()) || userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Username/Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        UUID id = UUID.randomUUID();
        CompletableFuture completableFuture = this.commandGateway.send(new CreateUserCommand(
                id,
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        ));

        try{
            completableFuture.get();
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/user/{id}")
                    .buildAndExpand(id).toUri();

            return ResponseEntity.created(location).body(new UserCreatedResponse(signUpRequest.getUsername(),signUpRequest.getEmail()));
        }catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

//    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest signInRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        signInRequest.getUsernameOrEmail(),
//                        signInRequest.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(authentication);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
//    }
}
