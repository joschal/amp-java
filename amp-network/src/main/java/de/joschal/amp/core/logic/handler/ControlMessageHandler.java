package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.control.Goodbye;
import de.joschal.amp.core.entities.messages.control.GoodbyeAck;
import de.joschal.amp.core.entities.messages.control.Hello;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.AbstractRouter;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.entities.network.Route;
import de.joschal.amp.core.inbound.ILinkLocalMessageReceiver;
import de.joschal.amp.core.logic.AddressManager;
import de.joschal.amp.core.logic.router.Router;
import de.joschal.amp.core.logic.sender.MessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ControlMessageHandler implements ILinkLocalMessageReceiver {

    private Address localAddress;
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
        log.info("Received Hello");

        // Triggers ZAL/AQ
        // Send address Advertisement if source address is 0
        if (helloMessage.getSourceAddress().getValue() == 0) {

            PoolAdvertisement poolAdvertisement;
            if (addressManager.isAPoolAvailable()) {
                List<AddressPool> reservedAddressPools = addressManager.reserveAddressPools(source);
                poolAdvertisement = new PoolAdvertisement(localAddress, Address.undefined(), reservedAddressPools);

            } else {
                // If no addresses are available, send empty advertisement
                poolAdvertisement = new PoolAdvertisement(localAddress, Address.undefined(), Collections.emptyList());
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
        GoodbyeAck goodbyeAck = new GoodbyeAck(localAddress, goodbye.getSourceAddress());
        messageSender.sendMessageToNeighbor(goodbyeAck, source);

        addressManager.revokeAddressPool(source);
        router.deleteRoute(goodbye.getSourceAddress());

    }

    private void handleGoodbyeAckMessage(GoodbyeAck message) {
        log.info("Received GoodbyeAck");
    }
}
