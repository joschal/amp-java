package de.joschal.amp.sim.core.logic;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.IDatagramController;
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
