package movie.festival.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreatedResponse {
    private String email;
    private String userName;
}
