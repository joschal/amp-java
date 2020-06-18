package de.joschal.amp.core.entities.messages.data;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class AcknowledgedDatagram extends AbstractDataMessage {

    private UUID uuid;
    private String payload;

    public AcknowledgedDatagram(Address sourceAddress, Address destinationAddress, int hopLimit, UUID uuid, String payload) {
        super(sourceAddress, destinationAddress, hopLimit);
        this.uuid = uuid;
        this.payload = payload;
    }

}
