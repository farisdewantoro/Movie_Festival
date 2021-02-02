package movie.festival.event;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDeletedEvent {
    private final UUID id;
}
