package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PoolAssigned extends AbstractAddressingMessage {

    private AddressPool addressPool;
    private boolean assigned;

    public PoolAssigned(PoolAccepted accepted, boolean assigned) {
        super(accepted.getDestinationAddress(), new Address(0), 1);
        this.addressPool = accepted.getAddressPool();
        this.assigned = assigned;
    }
}
