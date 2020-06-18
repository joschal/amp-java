package de.joschal.amp.core.entities.network.routing;

import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.io.NetworkInterface;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@ToString
public class Route {

    public static final long DEFAULT_TIMEOUT_MS = 60_000; // 60s

    public Route(long timeoutinMs, NetworkInterface networkInterface, Address address, int hops) {
        this.creationTime = DateTime.now();
        this.timeoutInMs = timeoutinMs;
        this.networkInterface = networkInterface;
        this.address = address;
        this.hops = hops;
    }

    public Route(NetworkInterface networkInterface, Address address, int hops) {
        this(DEFAULT_TIMEOUT_MS, networkInterface, address, hops);
    }

    private DateTime creationTime;
    private long timeoutInMs;

    public NetworkInterface networkInterface;
    public Address address;
    public int hops;

    public boolean isValid() {
        return this.creationTime.plus(timeoutInMs).isAfterNow();
    }

    public void refresh() {
        this.creationTime = DateTime.now();
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }
}
