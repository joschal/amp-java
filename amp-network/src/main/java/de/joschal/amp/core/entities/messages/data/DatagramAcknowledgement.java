package de.joschal.amp.core.entities.messages.data;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class DatagramAcknowledgement extends AbstractDataMessage {

    private UUID uuid;

    public DatagramAcknowledgement(Address sourceAddress, Address destinationAddress, int hopLimit, UUID uuid) {
        super(sourceAddress, destinationAddress, hopLimit);
        this.uuid = uuid;
    }

    public DatagramAcknowledgement(AcknowledgedDatagram acknowledgedDatagram) {
        super(acknowledgedDatagram.getDestinationAddress(), acknowledgedDatagram.getSourceAddress(), acknowledgedDatagram.getHopLimit());
        this.uuid = acknowledgedDatagram.getUuid();
    }

}
