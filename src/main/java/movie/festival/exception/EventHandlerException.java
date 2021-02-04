package movie.festival.exception;

import org.axonframework.eventhandling.*;

public class EventHandlerException implements ErrorHandler, ListenerInvocationErrorHandler {
    @Override
    public void handleError(ErrorContext errorContext) throws Exception {
        //TODO: store the event and replay the event once we have remedied the cause of the failure
        // https://stackoverflow.com/questions/66029031/axon-stuck-in-1-event-after-error-occurred/66040883?noredirect=1#comment116771778_66040883
        System.out.println(errorContext.error().toString());

    }

    @Override
    public void onError(Exception e, EventMessage<?> eventMessage, EventMessageHandler eventMessageHandler) throws Exception {
        System.out.println(e.toString());
    }
}
