package de.joschal.mdp.simulaiton;

import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import de.joschal.mdp.sim.DotParsing;
import org.junit.Test;

import java.util.HashMap;

public class NetFromFileStaticRouting {

    @Test
    public void netFromFileStaticRouting() {

        HashMap<Address, Node> nodes = DotParsing.readGraphFromFile("/123.dot", null, null);

        Node node1 = nodes.get(new Address("1"));
        Node node2 = nodes.get(new Address("2"));
        Node node3 = nodes.get(new Address("3"));

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
