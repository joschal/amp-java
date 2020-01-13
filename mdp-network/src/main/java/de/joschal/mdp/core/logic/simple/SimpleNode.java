package de.joschal.mdp.core.logic.simple;

import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.messages.control.Hello;
import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ToString
public class SimpleNode extends AbstractNode {

    public SimpleNode(String id, AbstractRouter router, AddressPool... addressPools) {
        super(id, router, addressPools);
    }

    // For testing
    public void action(String message, Address destination) {
        log.info("Action triggered in node {}", this.id);
        this.router.sendDatagram(message, destination);
    }

    public void bootSequence(){

        // Get address from
        Address selfAssigned = this.addressManager.assignAddressToSelf();

        if (selfAssigned == null){

            for (NetworkInterface networkInterface : this.networkInterfaces){
                networkInterface.sendMessage(new Hello());
            }
        }


    }
}
