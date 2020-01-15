package de.joschal.mdp.core.entities.messages.control;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

/**
 * Destination Address is always empty, since this is the first message of a given connection and there is no way to
 * know the destination address.
 *
 * Two cases for the source address are possible:
 * 1.   sourceAddress is empty (0). This means, that the current node does not have an address yet and therefore
 *      implicitly requests an address pool from the destination node
 * 2.   sourceAddress is populated. This means the node does have an assigned address (pool) and establishes an
 *      additional mesh connection
 *
 * HopLimit is always 1, since the message MUST NOT be forwarded
 */
@ToString
public class Hello extends AbstractControlMessage {

    /**
     * Use, if the node already has an address assigned
     * @param sourceAddress Currently assigned address
     */
    public Hello(Address sourceAddress) {
        super(sourceAddress, new Address(0), 1);
    }

    /**
     * Use on startup, when no address is assigned yet.
     * This will trigger the ZAL/AQ Algorithm
     */
    public Hello() {
        super(new Address(0), new Address(0), 1);
    }





}
