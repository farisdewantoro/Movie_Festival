package movie.festival.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangePasswordRequest {
    private final UUID id;
    private final String email;
    private String password;
}
