package movie.festival.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import movie.festival.command.DeleteUserCommand;
import movie.festival.model.User;
import movie.festival.payload.request.ChangePasswordRequest;
import movie.festival.payload.response.EmailAndUsernameResponse;
import movie.festival.repository.UserRepository;
import movie.festival.service.UserCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@Api
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    private final UserCommandService userCommandService;

    @GetMapping
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(value="id") UUID id){
        return userRepository.findById(id).orElseThrow(()->new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user is not found"
        ));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(DeleteUserCommand deleteUserCommand){
        return userCommandService.deleteUser(deleteUserCommand);
    }

    @PutMapping("/api/user/password")
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest){
        return userCommandService.changePassword(changePasswordRequest);
    }

    @GetMapping("/email")
    public List<String> getAllEmail(){return userRepository.getAllEmail();}

    @GetMapping("/emailAndUsername")
    public List<EmailAndUsernameResponse> getAllEmailAndUsername(){return userRepository.getAllEmailAndUsername();}
}
