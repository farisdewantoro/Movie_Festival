package movie.festival.event;

import lombok.Data;

import java.util.UUID;

@Data
public class UserCreatedEvent {
    private final UUID id;
    private final String fullName;
    private final String userName;
    private final String email;
    private final String password;
}
