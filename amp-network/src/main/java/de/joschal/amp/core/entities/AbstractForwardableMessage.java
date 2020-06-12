package de.joschal.amp.core.entities;

import de.joschal.amp.core.entities.network.AbstractNode;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public abstract class AbstractForwardableMessage extends AbstractMessage implements Cloneable {

    // Added header fields for forwardable messages
    protected int hopLimit;
    protected int hopCounter;

    public AbstractForwardableMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress);
        this.hopLimit = hopLimit;
        this.hopCounter = 0;
    }

    // Debugging tool
    protected List<String> tracerouteList = new LinkedList<>();

    /**
     * Must be invoked by every node when receiving a message.
     * @return true, if message may be processed. False if message must be dropped.
     */
    public boolean hop(String nodeId) {
        tracerouteList.add(nodeId);
        hopCounter++;
        return hopCounter == hopLimit;
    }
}
