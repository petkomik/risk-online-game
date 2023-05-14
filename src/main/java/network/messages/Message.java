package network.messages;

import java.io.Serializable;

/**
 * Base class for Message Packet.
 *
 * @author srogalsk
 */



public abstract class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private MessageType messageType;

  public Message(MessageType type) {
    this.messageType = type;
  }

  public MessageType getMessageType() {
    return this.messageType;
  }
}
