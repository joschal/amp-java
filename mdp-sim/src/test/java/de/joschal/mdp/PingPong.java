package de.joschal.mdp;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.LoggingNode;
import de.joschal.mdp.core.logic.simple.NonForwardingRouter;
import org.junit.Before;
import org.junit.Test;

public class PingPong {

    Address aAlice;
    Address aBob;

    NonForwardingRouter rAlice;
    NonForwardingRouter rBob;

    LoggingNode alice;
    LoggingNode bob;

    NetworkInterface iAlice;
    NetworkInterface iBob;

    DataLink dataLink;

    @Before
    public void setUp() {
        aAlice = new Address(1);
        aBob = new Address(2);

        rAlice = new NonForwardingRouter();
        rBob = new NonForwardingRouter();

        alice = new LoggingNode("Alice", aAlice, rAlice);
        bob = new LoggingNode("Bob", aBob, rBob);

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
