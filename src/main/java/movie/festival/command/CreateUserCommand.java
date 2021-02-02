package movie.festival.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand {
    @TargetAggregateIdentifier
    private UUID id;

    @NotBlank
    @Size(max = 40)
    private String fullName;

    @NotBlank
    @Size(max = 20)
    private String userName;

    @NotBlank
    @Email
    @Size(min = 5,max = 40)
    private String email;

    @NotBlank
    @Size(max=100)
    private String password;
}
