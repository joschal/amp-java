package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Datagram;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataLink {

    private String name;
    private NetworkInterface A;
    private NetworkInterface B;

    public DataLink(String name, NetworkInterface a, NetworkInterface b) {
        this.name = name;
        this.A = a;
        this.B = b;

        A.setDataLink(this);
        B.setDataLink(this);

    }

    boolean exchange(Datagram datagram, NetworkInterface sender) {

        if (sender == A) {
            return B.receiveDatagram(datagram);
        } else if (sender == B) {
            return A.receiveDatagram(datagram);
        } else {
            log.error("Something went horribly wrong in the data link layer! Sender: {} Datagram {}", sender, datagram);
            throw new RuntimeException("Something went horribly wrong in the data link layer!");
        }
    }

}
