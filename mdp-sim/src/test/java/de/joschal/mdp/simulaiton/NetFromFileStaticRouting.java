package de.joschal.mdp.simulaiton;

import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.SimpleNode;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import org.junit.Test;

public class NetFromFileStaticRouting {

    @Test
    public void netFromFileStaticRouting() {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        SimpleNode simpleNode1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode simpleNode2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode simpleNode3 = (SimpleNode) graph.getNodebyId("3");

        simpleNode1.setAddress(new Address("1"));
        simpleNode2.setAddress(new Address("2"));
        simpleNode3.setAddress(new Address("3"));

        simpleNode1.addRoute(new Route(simpleNode1.getInterfaces().get(0), simpleNode2.getAddress()));
        simpleNode1.addRoute(new Route(simpleNode1.getInterfaces().get(0), simpleNode3.getAddress()));

        simpleNode2.addRoute(new Route(simpleNode2.getInterfaces().get(1), simpleNode3.getAddress()));
        simpleNode2.addRoute(new Route(simpleNode2.getInterfaces().get(0), simpleNode1.getAddress()));

        simpleNode3.addRoute(new Route(simpleNode2.getInterfaces().get(1), simpleNode1.getAddress()));
        simpleNode3.addRoute(new Route(simpleNode2.getInterfaces().get(1), simpleNode1.getAddress()));

        simpleNode1.action("1->3", simpleNode3.getAddress());
        System.out.println("-------");
        simpleNode3.action("3->1", simpleNode1.getAddress());
    }
}
