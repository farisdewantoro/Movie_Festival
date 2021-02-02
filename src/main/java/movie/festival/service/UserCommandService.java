package movie.festival.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie.festival.command.ChangeUserPasswordCommand;
import movie.festival.command.CreateUserCommand;
import movie.festival.command.DeleteUserCommand;
import movie.festival.event.UserDeletedEvent;
import movie.festival.payload.request.ChangePasswordRequest;
import movie.festival.payload.response.ApiResponse;
import movie.festival.payload.request.SignUpRequest;
import movie.festival.payload.response.UserCreatedResponse;
import movie.festival.repository.UserRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/{id}")
                .buildAndExpand(id).toUri();

        return this.handleAsync(completableFuture, ResponseEntity.created(location).body(new UserCreatedResponse(signUpRequest.getUsername(),signUpRequest.getEmail())));

    }

    public ResponseEntity<?> deleteUser(DeleteUserCommand deleteUserCommand){
        if(!userRepository.existsById(deleteUserCommand.getId())) {
            return new ResponseEntity(new ApiResponse(false, String.format("user with id: %s is not found!",deleteUserCommand.getId().toString())),
                    HttpStatus.BAD_REQUEST);
        }
        CompletableFuture completableFuture = this.commandGateway.send(new UserDeletedEvent(deleteUserCommand.getId()));
        return this.handleAsync(completableFuture,ResponseEntity.ok(new ApiResponse(true,
                String.format("id:%s -  successfully deleted",deleteUserCommand.getId()))));

    }

    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest){
        if(!userRepository.existsById(changePasswordRequest.getId())) {
            return new ResponseEntity(new ApiResponse(false, String.format("user with id: %s is not found!",changePasswordRequest.getId().toString())),
                    HttpStatus.BAD_REQUEST);
        }
        changePasswordRequest.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        CompletableFuture completableFuture = this.commandGateway.send(
                new ChangeUserPasswordCommand(
                        changePasswordRequest.getId(),
                        changePasswordRequest.getEmail(),
                        changePasswordRequest.getPassword()));

        return this.handleAsync(completableFuture,ResponseEntity.ok(new ApiResponse(true,
                String.format("id:%s - password successfully updated",changePasswordRequest.getId()))));
    }

    private ResponseEntity<?> handleAsync(CompletableFuture completableFuture,ResponseEntity result){
        try{
            completableFuture.get();
            return result;
        }catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity(new ApiResponse(false,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
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
