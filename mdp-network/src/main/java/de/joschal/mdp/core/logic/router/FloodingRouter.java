package de.joschal.mdp.core.logic.router;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.Optional;

public class FloodingRouter extends AbstractRouter {

    @Override
    protected Optional<AbstractMessage> forwardMessage(AbstractMessage message) {
        return Optional.empty();
    }

    @Override
    public Optional<AbstractMessage> sendDatagram(String message, Address destination) {
        return Optional.empty();
    }

    @Override
    public Optional<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
        return Optional.empty();
    }

    @Override
    public Optional<AbstractMessage> sendMessage(AbstractMessage abstractMessage) {
        return Optional.empty();
    }
}
