package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.control.Goodbye;
import de.joschal.amp.core.entities.messages.control.GoodbyeAck;
import de.joschal.amp.core.entities.messages.control.Hello;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;
import de.joschal.amp.core.entities.network.routing.AbstractRouter;
import de.joschal.amp.core.inbound.layer3.ILinkLocalMessageReceiver;
import de.joschal.amp.core.logic.AddressManager;
import de.joschal.amp.core.logic.controlplane.MessageSender;
import de.joschal.amp.io.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ControlMessageHandler implements ILinkLocalMessageReceiver {

    private AbstractNode node;
    private AddressManager addressManager;
    private MessageSender messageSender;
    private AbstractRouter router;

    @Override
    public void handleMessage(AbstractMessage message, NetworkInterface source) {

        if (message instanceof Hello) {
            handleHelloMessage((Hello) message, source);
        } else if (message instanceof Goodbye) {
            handleGoodbyeMessage((Goodbye) message, source);
        } else if (message instanceof GoodbyeAck) {
            handleGoodbyeAckMessage((GoodbyeAck) message);
        }

    }

    private void handleHelloMessage(Hello helloMessage, NetworkInterface source) {
        log.info("[{}] Received Hello", this.node.getId());

        // Triggers ZAL/AQ
        // Send address Advertisement if source address is 0
        if (helloMessage.getSourceAddress().getValue() == 0) {

            PoolAdvertisement poolAdvertisement;
            if (addressManager.isAPoolAvailable()) {
                List<AddressPool> reservedAddressPools = addressManager.reserveAddressPools(source);
                poolAdvertisement = new PoolAdvertisement(node.getAddress(), Address.undefined(), reservedAddressPools);

            } else {
                // If no addresses are available, send empty advertisement
                poolAdvertisement = new PoolAdvertisement(node.getAddress(), Address.undefined(), Collections.emptyList());
            }
            messageSender.sendMessageToNeighbor(poolAdvertisement, source);
        } else {
            // Node annunces itself
            // May be part of ZAL/AQ
            // if addresses were reserved, they need to be revoked
            addressManager.revokeAddressPool(source);
        }
    }

    private void handleGoodbyeMessage(Goodbye goodbye, NetworkInterface source) {
        log.info("Received Goodbye");
        GoodbyeAck goodbyeAck = new GoodbyeAck(node.getAddress(), goodbye.getSourceAddress());
        messageSender.sendMessageToNeighbor(goodbyeAck, source);

        addressManager.revokeAddressPool(source);
        router.deleteRoute(goodbye.getSourceAddress());

    }

    private void handleGoodbyeAckMessage(GoodbyeAck message) {
        log.info("Received GoodbyeAck");
    }
}
