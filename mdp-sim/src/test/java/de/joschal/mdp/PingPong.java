package de.joschal.mdp;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.NonForwardingRouter;
import de.joschal.mdp.core.logic.simple.SimpleNode;
import org.junit.Before;
import org.junit.Test;

public class PingPong {

    Address aAlice;
    Address aBob;

    NonForwardingRouter rAlice;
    NonForwardingRouter rBob;

    SimpleNode alice;
    SimpleNode bob;

    NetworkInterface iAlice;
    NetworkInterface iBob;

    DataLink dataLink;

    @Before
    public void setUp() {
        aAlice = new Address("Alice");
        aBob = new Address("Bob");

        rAlice = new NonForwardingRouter();
        rBob = new NonForwardingRouter();

        alice = new SimpleNode("Alice", aAlice, rAlice);
        bob = new SimpleNode("Bob", aBob, rBob);

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
