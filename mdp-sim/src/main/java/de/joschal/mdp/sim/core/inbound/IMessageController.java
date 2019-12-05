package de.joschal.mdp.sim.core.inbound;

public interface IMessageController {

    void sendMessage(String source, String destination, String message);

}
