package de.joschal.amp.sim.core.inbound;

public interface IMessageController {

    void sendMessage(String source, String destination, String message);

}
