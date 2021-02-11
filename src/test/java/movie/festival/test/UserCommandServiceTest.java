package movie.festival.test;

import movie.festival.command.CreateUserCommand;
import movie.festival.payload.request.SignUpRequest;
import movie.festival.payload.response.UserCreatedResponse;
import movie.festival.repository.UserRepository;
import movie.festival.service.UserCommandService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class UserCommandServiceTest {
    private CommandGateway commandGateway;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SignUpRequest signUpRequest;

    @BeforeEach
    public void setup(){
        commandGateway = Mockito.mock(CommandGateway.class);
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        signUpRequest = new SignUpRequest("testName","testUserName","test@email.com","testPassword123");
    }

    @Test
    public void createUser_OnSignUpRequestAsParam_SendCreateUserCommand(){
        UserCommandService userCommandService = new UserCommandService(commandGateway,userRepository,passwordEncoder);
        Mockito.when(commandGateway.send(Mockito.any()))
                .thenReturn(CompletableFuture.completedFuture(UUID.randomUUID()));
        Mockito.when(userRepository.existsByUserName(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        userCommandService.createUser(new SignUpRequest("testName","testUserName","test@email.com","testPassword123"));
        Mockito.verify(commandGateway,Mockito.times(1)).send(ArgumentMatchers.any(CreateUserCommand.class));
    }

    @Test
    public void createUser_OnSignUpRequestAsParam_ReturnUserCreatedResponse(){
        final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
        UUID uuid = UUID.fromString(id);

        MockedStatic<UUID> idDummy = Mockito.mockStatic(UUID.class);
        idDummy.when(UUID::randomUUID).thenReturn(uuid);

        Mockito.when(commandGateway.send(Mockito.any())).thenReturn(CompletableFuture.completedFuture(uuid));
        Mockito.when(userRepository.existsByUserName(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        UserCommandService userCommandService = new UserCommandService(commandGateway,userRepository,passwordEncoder);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/{id}")
                .buildAndExpand(id).toUri();

        ResponseEntity<?> actual =  userCommandService.createUser(signUpRequest);
        ResponseEntity<?> expected = ResponseEntity.created(location).body(new UserCreatedResponse(signUpRequest.getUsername(),signUpRequest.getEmail()));
        Assertions.assertEquals(expected,actual);

   }


}
