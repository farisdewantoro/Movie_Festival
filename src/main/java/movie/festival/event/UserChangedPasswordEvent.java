package movie.festival.event;

import lombok.Data;

import java.util.UUID;

@Data
public class UserChangedPasswordEvent {
    private final UUID id;
    private final String email;
    private final String password;
}
