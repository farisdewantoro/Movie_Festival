package movie.festival.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreatedResponse {
    private String email;
    private String userName;
}
