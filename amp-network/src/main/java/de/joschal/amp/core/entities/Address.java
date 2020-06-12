package de.joschal.amp.core.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Address implements Serializable {

    // long -> 64 bits
    private long value;

    @Override
    public String toString() {
        return Long.toString(value);
    }

    public static Address undefined(){return new Address(0);}
}
