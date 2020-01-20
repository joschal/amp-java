package de.joschal.mdp.core.entities;

import de.joschal.mdp.core.entities.network.AbstractNode;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public abstract class AbstractMessage implements Cloneable {

    public AbstractMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.hopLimit = hopLimit;
    }

    private Address sourceAddress;

    private Address destinationAddress;

    protected int hopLimit;

    protected int hopCounter;

    protected List<String> tracerouteList = new LinkedList<>();

    public boolean hop(AbstractNode node) {
        tracerouteList.add(node.getId());
        hopCounter++;
        hopLimit--;
        return hopLimit == 0;
    }
}

