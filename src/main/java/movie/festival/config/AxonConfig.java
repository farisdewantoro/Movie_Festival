package movie.festival.config;

import movie.festival.exception.EventHandlerException;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Autowired
    public void configure(final EventProcessingConfigurer config) {
        EventHandlerException testErrorHandler = new EventHandlerException();
        config.registerErrorHandler("UserProjection", configuration -> testErrorHandler);
    }
}
