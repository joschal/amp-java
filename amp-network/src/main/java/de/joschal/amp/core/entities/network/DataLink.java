package de.joschal.amp.core.entities.network;

import de.joschal.amp.core.entities.AbstractMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Getter
public class DataLink {

    private String name;
    private NetworkInterface a;
    private NetworkInterface b;

    private Queue<AbstractMessage> aSendQueue;
    private Queue<AbstractMessage> bSendQueue;

    private int messageCounter = 0;

    public DataLink(String name, NetworkInterface a, NetworkInterface b) {
        this.name = name;
        this.a = a;
        this.b = b;

        this.a.setDataLink(this);
        this.b.setDataLink(this);

        aSendQueue = new LinkedList<>();
        bSendQueue = new LinkedList<>();
    }

    /**
     * Adds a message to the respectiv sendQueue of the interface
     *
     * @param message         message to send via the DataLink
     * @param senderInferface identificaiton of the sender interface
     * @return soccess of adding the message to the send queue
     */
    public boolean addToSendQueue(AbstractMessage message, NetworkInterface senderInferface) {

        if (senderInferface == a) {
            return aSendQueue.add(message);
        } else if (senderInferface == b) {
            return bSendQueue.add(message);
        }

        return false;
    }

    /**
     * Pop message from send queue and tansfer it to the other node
     * Uses round-robin logic to select the queue
     */
    public void transferMessage() {
        // both queues have pending messages
        if (aSendQueue.size() > 0 && bSendQueue.size() > 0) {

            // Alternate between send queues for fair distribution
            if (messageCounter % 2 == 0) {
                log.debug("Transferred message from {} to {} : [{}]", b.getNodeId(), a.getNodeId(), bSendQueue.peek());
                a.receiveMessage(bSendQueue.poll());
            } else {
                log.debug("Transferred message from {} to {} : [{}]", a.getNodeId(), b.getNodeId(), aSendQueue.peek());
                b.receiveMessage(aSendQueue.poll());
            }

            messageCounter++;
            return;
        }

        // only aSendQueue has pending messages
        if (aSendQueue.size() > 0) {
            log.debug("Transferred message from {} to {} : [{}]", a.getNodeId(), b.getNodeId(), aSendQueue.peek());
            b.receiveMessage(aSendQueue.poll());
            return;
        }

        // only bSendQueue has pending messages
        if (bSendQueue.size() > 0) {
            log.debug("Transferred message from {} to {} : [{}]", b.getNodeId(), a.getNodeId(), bSendQueue.peek());
            a.receiveMessage(bSendQueue.poll());
        }

        /*
        while (!aSendQueue.isEmpty()){
            log.debug("Transferred message from {} to {} : [{}]", a.getNodeId(), b.getNodeId(), aSendQueue.peek());
            b.receiveMessage(aSendQueue.poll());
        }

        while (!bSendQueue.isEmpty()){
            log.debug("Transferred message from {} to {} : [{}]", b.getNodeId(), a.getNodeId(), bSendQueue.peek());
            a.receiveMessage(bSendQueue.poll());
        }

        */
    }

    @Override
    public String toString() {
        return "DataLink{" +
                "name='" + name + '\'' +
                a.getNodeId() + " <-> " + b.getNodeId() + '}';
    }
}
