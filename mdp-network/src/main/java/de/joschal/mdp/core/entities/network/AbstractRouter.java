package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.outbound.IDatagramSender;
import de.joschal.mdp.core.outbound.IMessageSender;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractRouter implements IDatagramSender, IMessageSender {

    protected AbstractNode node;

    protected Set<Route> routingTable;

    void setNode(AbstractNode node) {
        this.node = node;
    }

    public void addRoute(Route route) {
        routingTable.add(route);
    }

    protected abstract List<AbstractMessage> forwardMessage(AbstractMessage message, NetworkInterface source);

    public void updateRoutingTable(NetworkInterface networkInterface, AbstractMessage message) {

        // 0 is not a routable address and is therefore not stored
        if (message.getSourceAddress().getValue() == 0) {
            return;
        }

        Optional<Route> optional = routingTable.stream()
                .filter(r -> r.getAddress().equals(message.getSourceAddress()))
                .findFirst();

        // a route to the source node is already known
        if (optional.isPresent()) {
            Route route = optional.get();
            route.refresh(); // update creation date to now

            // update the hop distance
            if (message.getHopCounter() < route.getHops()) {
                route.setHops(message.getHopCounter());
            }

            // if a faster route is found via a different network interface, update it
            if (networkInterface.equals(route.getNetworkInterface())) {
                route.setNetworkInterface(networkInterface);
            }

            // no route to source node known
            // create one
        } else {
            Route route = new Route(networkInterface, message.getSourceAddress(), message.getHopCounter());
            this.addRoute(route);
        }
    }

}
