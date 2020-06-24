package de.joschal.amp.sim.core.logic;

import de.joschal.amp.core.entities.messages.data.Datagram;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.IMessageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MessageController implements IMessageController {

    public static final int DEFAULT_DATAGRAM_HOP_LIMIT = 32;

    @Autowired
    private Graph graph;

    @Override
    public void sendDatagram(String source, String destination, String payload) {
        Datagram datagram = new Datagram(
                graph.getNodebyId(source).getAddress(),
                graph.getNodebyId(destination).getAddress(),
                DEFAULT_DATAGRAM_HOP_LIMIT,
                payload
        );

        graph.getNodebyId(source).getMessageSender().sendMessageViaKnownRoute(datagram);
    }
}
