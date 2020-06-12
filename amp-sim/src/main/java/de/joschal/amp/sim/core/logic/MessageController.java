package de.joschal.amp.sim.core.logic;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.messages.data.Datagram;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.IMessageController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static de.joschal.amp.sim.App.DEFAULT_MAX_HOPS;

@Slf4j
@AllArgsConstructor
public class MessageController implements IMessageController {

    private Graph graph;

    @Override
    public void sendMessage(String source, String destination, String message) {

    }
}
