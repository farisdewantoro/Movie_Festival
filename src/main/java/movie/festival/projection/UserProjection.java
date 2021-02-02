package movie.festival.projection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie.festival.event.UserChangedPasswordEvent;
import movie.festival.event.UserCreatedEvent;
import movie.festival.exception.AppException;
import movie.festival.model.Role;
import movie.festival.model.RoleName;
import movie.festival.model.User;
import movie.festival.repository.RoleRepository;
import movie.festival.repository.UserRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserProjection {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @EventHandler
    public void on(UserCreatedEvent event) throws Exception {
        log.debug("Handling a UserCreatedEvent {}", event);


            // Creating user's account
        User user = new User(
                event.getId(),
                event.getFullName(),
                event.getUserName(),
                event.getEmail(),
                event.getPassword()
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    @EventHandler
    public void on(UserChangedPasswordEvent event) throws Exception {
        log.debug("Handling a UserChangedPasswordEvent {}", event);
        Optional<User> optionalUser = this.userRepository.findById(event.getId());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setPassword(event.getPassword());
            this.userRepository.save(user);
        }else{
            String error = "Cannot found account number [" + event.getId() + "]";
            log.error(error);
            throw new Exception(error);
        }
    }
}
