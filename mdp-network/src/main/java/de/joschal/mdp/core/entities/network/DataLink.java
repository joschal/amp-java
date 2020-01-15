package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
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

    List<AbstractMessage> exchange(AbstractMessage message, NetworkInterface sender) {

        if (sender == A) {
            log.debug("{} --> {}", A.getName(), B.getName());
            return B.receiveMessage(message);
        } else if (sender == B) {
            log.debug("{} --> {}", B.getName(), A.getName());
            return A.receiveMessage(message);
        } else {
            log.error("Something went horribly wrong in the data link layer! Sender: {} Message: {}", sender, message);
            throw new RuntimeException("Something went horribly wrong in the data link layer!");
        }
    }

    @Override
    public String toString() {
        return "DataLink{" +
                "name='" + name + '\'' +
                A.getName() + " <-> " + B.getName() + '}';
    }
}
