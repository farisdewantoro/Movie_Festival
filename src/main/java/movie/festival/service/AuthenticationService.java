package movie.festival.service;

import movie.festival.payload.SignInRequest;
import movie.festival.payload.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> register(SignUpRequest signUpRequest);
    ResponseEntity<?> login(SignInRequest signInRequest);
}
