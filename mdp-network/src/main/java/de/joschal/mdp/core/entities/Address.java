package de.joschal.mdp.core.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Address {

    private int value;

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
