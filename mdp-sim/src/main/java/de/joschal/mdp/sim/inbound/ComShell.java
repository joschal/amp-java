package de.joschal.mdp.sim.inbound;

import de.joschal.mdp.sim.core.inbound.IMessageController;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@ShellCommandGroup
@AllArgsConstructor
public class ComShell {

    private IMessageController messageController;

    @ShellMethod(value = "Send message between nodes, identified by ID", key = "message")
    public void sendMessage(
            @ShellOption String sourceId,
            @ShellOption String destinationId,
            @ShellOption String message
    ) {
        messageController.sendMessage(sourceId, destinationId, message);
    }

}
