package de.joschal.amp.core.entities.network;

import de.joschal.amp.core.entities.AbstractForwardableMessage;
import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
public abstract class AbstractRouter {

    public static final int DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT = 16;

    public AbstractRouter(Address localAddress, Set<Route> routingTable, List<NetworkInterface> networkInterfaces) {
        this.localAddress = localAddress;
        this.routingTable = routingTable;
        this.networkInterfaces = networkInterfaces;
    }

    protected Address localAddress;
    protected Set<Route> routingTable;
    protected List<NetworkInterface> networkInterfaces;

    public abstract void sendRouteDiscovery(Address destination);

    public abstract void updateRoutingTable(NetworkInterface networkInterface, AbstractForwardableMessage message);

    public void addRoute(Route route) {
        // Filter out invalid adresses and the nodes own address
        if (route.getAddress().getValue() == 0 ||
                route.getAddress().getValue() == localAddress.getValue()) {
            return;
        }
        routingTable.add(route);
    }

    public Optional<Route> getRoute(AbstractMessage message) {
        return getRoute(message.getDestinationAddress());
    }

    public Optional<Route> getRoute(Address destination) {
        return routingTable.stream()
                .filter(r -> r.getAddress().equals(destination))
                .findFirst();
    }

    public void deleteRoute(Address destination){

        Optional<Route> optionalRoute = getRoute(destination);
        optionalRoute.ifPresent(route -> routingTable.remove(route));

    }
}
