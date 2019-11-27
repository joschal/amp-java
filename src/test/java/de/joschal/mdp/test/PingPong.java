package de.joschal.mdp.test;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import de.joschal.mdp.core.logic.simple.Router;
import org.junit.Before;
import org.junit.Test;

public class PingPong {

    Address aAlice;
    Address aBob;

    Router rAlice;
    Router rBob;

    Node alice;
    Node bob;

    NetworkInterface iAlice;
    NetworkInterface iBob;

    DataLink dataLink;

    @Before
    public void setUp() {
        aAlice = new Address("Alice");
        aBob = new Address("Bob");

        rAlice = new Router();
        rBob = new Router();

        alice = new Node(aAlice, rAlice);
        bob = new Node(aBob, rBob);

        iAlice = new NetworkInterface("iAlice");
        iBob = new NetworkInterface("iBob");

        new DataLink("dataLink", iAlice, iBob);

        alice.addNetworkInterface(iAlice);
        bob.addNetworkInterface(iBob);
    }

    @Test
    public void ping() {
        alice.action("foo", aBob);
    }
}
