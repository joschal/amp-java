package de.joschal.amp.core.entities.messages;

import de.joschal.amp.core.entities.AbstractMessage;

/**
 * Implementation of this Interface indicate, that a message is forwardable via multiple hops;
 */
public interface Forwardable {

    AbstractMessage cloneMessage();

}
