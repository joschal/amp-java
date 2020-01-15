package de.joschal.mdp.core.logic.simple;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.mdp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.mdp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.mdp.core.entities.messages.control.Hello;
import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


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

    public void bootSequence() {

        // Get address from
        Address selfAssigned = this.addressManager.assignAddressToSelf();

        if (selfAssigned == null) {

            List<PoolAdvertisement> advertisements = requestPoolAdvertisements();

            AddressPool assignedPool = getBestAddressPool(advertisements);

            if (assignedPool != null) {
                this.addressManager.addAddressPool(assignedPool);
                this.setAddress(this.addressManager.assignAddressToSelf());
                return;
            }
            log.error("Could not assign address");
            return;
        }
        this.setAddress(selfAssigned);
    }

    /**
     * Requests Address Pool Advertisements from adjacent nodes
     *
     * @return
     */
    private List<PoolAdvertisement> requestPoolAdvertisements() {

        List<PoolAdvertisement> advertisements = new LinkedList<>();

        for (NetworkInterface networkInterface : this.networkInterfaces) {
            Optional<AbstractMessage> response = networkInterface.sendMessage(new Hello());

            response.ifPresent(message -> {
                advertisements.add((PoolAdvertisement) message);
                this.router.addRoute(new Route(networkInterface, message.getSourceAddress()));
                log.info("Received address pool advertisement: {}");
            });

        }

        advertisements.sort(Comparator.reverseOrder());
        return advertisements;
    }

    private AddressPool getBestAddressPool(List<PoolAdvertisement> advertisements) {
        for (PoolAdvertisement advertisement : advertisements) {
            PoolAccepted accepted = new PoolAccepted(advertisement);
            Optional<AbstractMessage> response = this.router.sendMessage(accepted);

            if (response.isPresent()) {
                PoolAssigned assigned = (PoolAssigned) response.get();
                if (assigned.isAssigned()) {
                    return assigned.getAddressPool();
                }
            }
        }
        log.error("None of the desired address pools could be assigned");
        return null;
    }

}
