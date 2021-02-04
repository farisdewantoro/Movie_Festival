package movie.festival.test;

import movie.festival.command.ChangeUserPasswordCommand;
import movie.festival.command.CreateUserCommand;
import movie.festival.event.UserChangedPasswordEvent;
import movie.festival.event.UserCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UserAggregateTest {

    private FixtureConfiguration<UserAggregateTest> fixture;
    private UUID id;
    private static final String userName = "testUsername";
    private static final String email = "testEmail";
    private static final String password = "password";
    private static final String fullName = "testFullname";

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(UserAggregateTest.class);
        id = UUID.randomUUID();
    }

    @Test
    public void should_dispatch_userCreatedEvent_when_createUserCommand() {
        fixture.givenNoPriorActivity()
                .when(new CreateUserCommand(
                        id,
                        fullName,
                        userName,
                        email,
                        password)
                )
                .expectEvents(new UserCreatedEvent(
                        id,
                        fullName,
                        userName,
                        email,
                        password)
                );
    }

    @Test
    public void should_dispatch_userChangedPassword_event_when_ChangeUserCommand() {
        fixture.given(new UserCreatedEvent( id,
                    fullName,
                    userName,
                    email,
                    password))
                .when(new ChangeUserPasswordCommand(id,userName,password))
                .expectEvents(new UserChangedPasswordEvent(id,email,password));
    }
}
