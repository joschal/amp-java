package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class PoolAssigned extends AbstractAddressingMessage {

    private List<AddressPool> addressPools = new LinkedList<>();
    private boolean assigned;

    public PoolAssigned(PoolAccepted accepted, boolean assigned) {
        super(accepted.getDestinationAddress(), new Address(0), 1);
        this.addressPools.addAll(accepted.getAddressPools());
        this.assigned = assigned;
    }
}
