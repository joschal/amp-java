package de.joschal.mdp.core.entities.messages.data;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;

@Getter
@ToString
public class Datagram extends AbstractDataMessage {

    public Datagram(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

    private String payload;

    public Datagram(Address sourceAddress, Address destinationAddress, int hopLimit, String payload) {
        super(sourceAddress, destinationAddress, hopLimit);
        this.payload = payload;
    }

    // Only used for cloning the object
    protected Datagram(Datagram original) {
        this(new Address(original.getSourceAddress().getValue()), new Address(original.getDestinationAddress().getValue()), original.getHopLimit());
        this.hopCounter = original.getHopCounter();
        this.tracerouteList = new LinkedList<>(original.tracerouteList);
        this.payload = original.getPayload();
    }

    @Override
    public AbstractMessage cloneMessage() {
        return new Datagram(this);
    }
}
