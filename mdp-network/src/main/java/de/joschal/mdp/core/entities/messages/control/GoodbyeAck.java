package de.joschal.mdp.core.entities.messages.control;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

@ToString
public class GoodbyeAck extends AbstractControlMessage {

    public GoodbyeAck(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

}
