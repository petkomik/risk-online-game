package network.messages;

/**
 * The MessageServerCloseConnection class represents a message that is sent to notify the server to
 * close the connection. It serves as a signal for closing the connection between the client and the
 * server.
 *
 * author dignatov
 */
public class MessageServerCloseConnection extends Message {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a MessageServerCloseConnection object. This message notify.
   */

  public MessageServerCloseConnection() {
    super(MessageType.MessageServerCloseConnection);
  }


}
