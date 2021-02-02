package movie.festival.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movie.festival.command.ChangeUserPasswordCommand;
import movie.festival.command.CreateUserCommand;
import movie.festival.command.DeleteUserCommand;
import movie.festival.event.UserChangedPasswordEvent;
import movie.festival.event.UserCreatedEvent;
import movie.festival.event.UserDeletedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Aggregate
public class UserAggregate {
    @AggregateIdentifier
    private UUID id;
    private String fullName;
    private String userName;
    private String email;
    private String password;

    @CommandHandler
    public UserAggregate(CreateUserCommand command) {
        AggregateLifecycle.apply(
                new UserCreatedEvent(
                        command.getId(),
                        command.getFullName(),
                        command.getUserName(),
                        command.getEmail(),
                        command.getPassword()
                )
        );
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        this.id = event.getId();
        this.email = event.getEmail();
        this.fullName = event.getFullName();
        this.userName = event.getUserName();
    }

    @CommandHandler
    public void handle(ChangeUserPasswordCommand command){
        AggregateLifecycle.apply(
             new UserChangedPasswordEvent(command.getId(),command.getEmail(),command.getPassword())
        );
    }

    @CommandHandler
    public void handle(DeleteUserCommand command){
        AggregateLifecycle.apply(new UserDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event){
        AggregateLifecycle.markDeleted();

    }

    @EventSourcingHandler
    public void on(UserChangedPasswordEvent event){
        this.id = event.getId();
        this.email = event.getEmail();
        this.password = event.getPassword();
    }

}
