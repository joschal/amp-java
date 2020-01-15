package de.joschal.mdp.core.logic.handler;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.mdp.core.entities.messages.control.Goodbye;
import de.joschal.mdp.core.entities.messages.control.Hello;
import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.inbound.INetworkReceiver;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ControlMessageHandler implements INetworkReceiver {

    private AbstractNode node;

    @Override
    public Optional<AbstractMessage> handleMessage(AbstractMessage message, NetworkInterface networkInterface) {

        if (message instanceof Hello) {
            return handleHelloMessage((Hello) message, networkInterface);
        } else if (message instanceof Goodbye) {
            return handleGoodbyeMessage((Goodbye) message);
        }

        throw new RuntimeException("Something went wrong while resolving a control message");
    }

    private Optional<AbstractMessage> handleHelloMessage(Hello helloMessage, NetworkInterface networkInterface) {

        if (helloMessage.getSourceAddress().getValue() == 0) {

            if (node.getAddressManager().isPoolAvailable()) {
                // This early assignment of an address pool is not fully compliant with ZAL. It might result in address leakage. But this should be ok for the simulation
                PoolAdvertisement poolAdvertisement = new PoolAdvertisement(node.getAddress(), node.getAddressManager().assignAddressPool(networkInterface));
                // Send prefix advertisement
                return Optional.of(poolAdvertisement);
            } else {
                return Optional.empty();
            }
        } else {
            node.addRoute(new Route(networkInterface, helloMessage.getSourceAddress()));
        }
        return Optional.empty();
    }

    private Optional<AbstractMessage> handleGoodbyeMessage(Goodbye goodbye) {
        return Optional.empty();
    }


}
