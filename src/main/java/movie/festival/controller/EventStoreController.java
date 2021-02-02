package movie.festival.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import movie.festival.command.DeleteUserCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@Api
@RequestMapping("/api/eventStore")
public class EventStoreController {
    private final EventStore eventStore;
    private final CommandGateway commandGateway;

    @GetMapping
    public List<Object> listEventById(String id) {
        return this.eventStore.readEvents(formatUuid(id).toString())
                .asStream()
                .map(Message::getPayload)
                .collect(Collectors.toList());
    }

    @PostMapping
    public void deleteEventById(String id){
        this.eventStore.readEvents(formatUuid(id).toString()).remove();
    }

    @DeleteMapping
    public CompletableFuture<?> deleteUserEvent(DeleteUserCommand deleteUserCommand){
        return commandGateway.send(deleteUserCommand);

    }

    public static UUID formatUuid(String id) {
        id = id.replace("-", "");
        String formatted = String.format(
                id.substring(0, 8) + "-" +
                        id.substring(8, 12) + "-" +
                        id.substring(12, 16) + "-" +
                        id.substring(16, 20) + "-" +
                        id.substring(20, 32)
        );
        return UUID.fromString(formatted);
    }
}
