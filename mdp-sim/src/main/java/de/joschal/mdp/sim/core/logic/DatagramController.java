package de.joschal.mdp.sim.core.logic;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.core.inbound.IDatagramController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class DatagramController implements IDatagramController {

    Graph graph;

    @Override
    public void sendDatagram(AbstractMessage datagram) {
        log.error("Not implemented yet");
    }
}
