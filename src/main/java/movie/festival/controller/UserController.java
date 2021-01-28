package movie.festival.controller;

import movie.festival.model.User;
import movie.festival.payload.EmailAndUsernameResponse;
import movie.festival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/email")
    public List<String> getAllEmail(){return userRepository.getAllEmail();}

    @GetMapping("/emailAndUsername")
    public List<EmailAndUsernameResponse> getAllEmailAndUsername(){return userRepository.getAllEmailAndUsername();}
}
