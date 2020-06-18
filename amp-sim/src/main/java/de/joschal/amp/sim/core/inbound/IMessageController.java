package de.joschal.amp.sim.core.inbound;

public interface IMessageController {

    void sendDatagram(String source, String destination, String message);

}
