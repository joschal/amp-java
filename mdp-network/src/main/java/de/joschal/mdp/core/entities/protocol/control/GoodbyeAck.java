package de.joschal.mdp.core.entities.protocol.control;

import de.joschal.mdp.core.entities.protocol.Address;

public class GoodbyeAck extends AbstractControlMessage {

    public GoodbyeAck(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

}
