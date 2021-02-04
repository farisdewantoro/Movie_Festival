package movie.festival.test.parameterResolver;

import movie.festival.event.UserCreatedEvent;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.UUID;

public class UserCreatedEventResolver implements ParameterResolver {

    UserCreatedEvent event =new UserCreatedEvent(
            UUID.randomUUID(),
            "testFullName",
            "testUserName",
            "testEmail",
            "testPassword"
            );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UserCreatedEvent.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return event;
    }
}
