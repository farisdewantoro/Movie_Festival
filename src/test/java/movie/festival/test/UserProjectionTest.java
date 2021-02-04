package movie.festival.test;

import movie.festival.event.UserCreatedEvent;
import movie.festival.model.Role;
import movie.festival.model.RoleName;
import movie.festival.model.User;
import movie.festival.projection.UserProjection;
import movie.festival.repository.RoleRepository;
import movie.festival.repository.UserRepository;
import movie.festival.test.parameterResolver.UserCreatedEventResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;


@ExtendWith(UserCreatedEventResolver.class)
public class UserProjectionTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setup(){
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

    }

    @Test
    public void should_create_read_model_user(UserCreatedEvent event) throws Exception {
        Role role = new Role(RoleName.ROLE_USER);


        Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).
                thenReturn(Optional.of(role));

        Mockito.when(passwordEncoder.encode(event.getPassword()))
                .thenReturn("encodedPassword");

        User user = new User(event.getId(),
                event.getFullName(),
                event.getUserName(),
                event.getEmail(),
                "encodedPassword");

        user.setRoles(Collections.singleton(role));

        UserProjection userProjection = new UserProjection(userRepository,roleRepository,passwordEncoder);
        userProjection.on(new UserCreatedEvent(
                event.getId(),
                event.getFullName(),
                event.getUserName(),
                event.getEmail(),
                event.getPassword()));

        Mockito.verify(userRepository,Mockito.atLeastOnce())
                .save(ArgumentMatchers.refEq(user));
    }
}
