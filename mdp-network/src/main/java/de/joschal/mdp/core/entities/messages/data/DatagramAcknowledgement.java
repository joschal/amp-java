package de.joschal.mdp.core.entities.messages.data;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

import java.util.LinkedList;

@ToString
public class DatagramAcknowledgement extends AbstractDataMessage {

    public DatagramAcknowledgement(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

    // Only used for cloning the object
    protected DatagramAcknowledgement(DatagramAcknowledgement original) {
        this(new Address(original.getSourceAddress().getValue()), new Address(original.getDestinationAddress().getValue()), original.getHopLimit());
        this.hopCounter = original.getHopCounter();
        this.tracerouteList = new LinkedList<>(original.tracerouteList);
    }

    @Override
    public AbstractMessage cloneMessage() {
        return new DatagramAcknowledgement(this);
    }

}
