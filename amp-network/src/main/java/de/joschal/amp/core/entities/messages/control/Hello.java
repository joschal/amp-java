package de.joschal.amp.core.entities.messages.control;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.ToString;

/**
 * Destination Address is always empty, since this is the first message of a given connection and there is no way to
 * know the destination address.
 * <p>
 * Two cases for the source address are possible:
 * 1.   sourceAddress is empty (0). This means, that the current node does not have an address yet and therefore
 * implicitly requests an address pool from the destination node
 * 2.   sourceAddress is populated. This means the node does have an assigned address (pool) and establishes an
 * additional mesh connection
 */
@ToString
public class Hello extends AbstractControlMessage {

    /**
     * Use, if the node already has an address assigned
     *
     * @param sourceAddress Currently assigned address
     */
    public Hello(Address sourceAddress) {
        super(sourceAddress, Address.undefined());
    }

    /**
     * Use on startup, when no address is assigned yet.
     * This will trigger the ZAL/AQ Algorithm
     */
    public Hello() {
        super(Address.undefined(), Address.undefined());
    }


}
