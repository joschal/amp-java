package de.joschal.mdp.sim.core.logic;

import de.joschal.mdp.core.entities.protocol.Datagram;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.core.inbound.IDatagramController;
import de.joschal.mdp.sim.core.inbound.IMessageController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static de.joschal.mdp.sim.App.DEFAULT_MAX_HOPS;

@Slf4j
@AllArgsConstructor
public class MessageController implements IMessageController {

    private IDatagramController datagramController;
    private Graph graph;

    @Override
    public void sendMessage(String source, String destination, String message) {

        Datagram datagram = new Datagram(
                graph.getNodebyId(source).getAddress(),
                graph.getNodebyId(destination).getAddress(),
                DEFAULT_MAX_HOPS,
                message);

        datagramController.sendDatagram(datagram);

    }
}
