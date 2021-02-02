package movie.festival.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import movie.festival.payload.SignUpRequest;
import movie.festival.service.UserCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Api
@AllArgsConstructor
public class AuthController {

    private final UserCommandService userCommandService;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
       return userCommandService.createUser(signUpRequest);
    }

}
