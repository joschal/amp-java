package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.Address;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Route {

    public NetworkInterface networkInterface;
    public Address address;

}
