package de.joschal.mdp.simulaiton;

import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import de.joschal.mdp.core.logic.staticrouting.StaticRouter;
import org.junit.Test;

import static de.joschal.mdp.sim.Setup.linkInterfaces;
import static de.joschal.mdp.sim.Setup.setUpNode;

public class StaticNetStaticRouting {

    @Test
    public void circle() {
        Node node1 = setUpNode(
                new Address("1"),
                new StaticRouter(),
                new NetworkInterface("1a"),
                new NetworkInterface("1b"));

        Node node2 = setUpNode(
                new Address("2"),
                new StaticRouter(),
                new NetworkInterface("2a"),
                new NetworkInterface("2b"));

        Node node3 = setUpNode(
                new Address("3"),
                new StaticRouter(),
                new NetworkInterface("3a"),
                new NetworkInterface("3b"));

        // circular link
        linkInterfaces(node1.getInterfaces().get(0), node2.getInterfaces().get(0));
        linkInterfaces(node2.getInterfaces().get(1), node3.getInterfaces().get(0));

        node1.addRoute(new Route(node1.getInterfaces().get(0), node2.getAddress()));
        node1.addRoute(new Route(node1.getInterfaces().get(0), node3.getAddress()));

        node2.addRoute(new Route(node2.getInterfaces().get(1), node3.getAddress()));
        node2.addRoute(new Route(node2.getInterfaces().get(0), node1.getAddress()));

        node3.addRoute(new Route(node2.getInterfaces().get(1), node1.getAddress()));
        node3.addRoute(new Route(node2.getInterfaces().get(1), node1.getAddress()));

        node1.action("1->3", node3.getAddress());
        System.out.println("-------");
        node3.action("3->1", node1.getAddress());

    }
}
