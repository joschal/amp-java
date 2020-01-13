package de.joschal.mdp.core.logic.handler;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.mdp.core.entities.messages.control.Goodbye;
import de.joschal.mdp.core.entities.messages.control.Hello;
import de.joschal.mdp.core.inbound.INetworkReceiver;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ControlMessageHandler implements INetworkReceiver {

    private AbstractNode node;

    @Override
    public void handleMessage(AbstractMessage message, NetworkInterface networkInterface) {

        if (message instanceof Hello) {
            handleHelloMessage((Hello) message, networkInterface);
        } else if (message instanceof Goodbye) {
            handleGoodbyeMessage((Goodbye) message);
        }

        throw new RuntimeException("Something went wrong while resolving a control message");
    }

    private void handleHelloMessage(Hello helloMessage, NetworkInterface networkInterface) {

        if (helloMessage.getSourceAddress().getValue() == 0) {

            // This early assignment of an address pool is not fully compliant with ZAL. It might result in address leakage. But this should be ok for the simulation
            PoolAdvertisement poolAdvertisement = new PoolAdvertisement(node.getAddress(), new Address(0), node.getAddressManager().assignAddressPool(networkInterface));
            // Send prefix advertisement
            node.addressingMessageSender.sendAddressAdvertisement(poolAdvertisement);
        } else {
            node.addRoute(new Route(networkInterface, helloMessage.getSourceAddress()));
        }

    }

    private void handleGoodbyeMessage(Goodbye goodbye) {

    }


}
