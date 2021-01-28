package movie.festival.controller;

import movie.festival.payload.SignInRequest;
import movie.festival.payload.SignUpRequest;
import movie.festival.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
       return authenticationService.register(signUpRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody SignInRequest signInRequest){
        return authenticationService.login(signInRequest);
    }
}
