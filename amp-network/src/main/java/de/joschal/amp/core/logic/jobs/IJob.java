package de.joschal.amp.core.logic.jobs;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.io.NetworkInterface;

public interface IJob {

    void init();

    void tick();

    void tearDown();

    void receiveMessage(AbstractMessage message, NetworkInterface source);

}
