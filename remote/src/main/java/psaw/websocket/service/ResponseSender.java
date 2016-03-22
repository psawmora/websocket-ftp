package psaw.websocket.service;

import java.nio.ByteBuffer;

/**
 * <p>
 * <code>ResponseSender</code> -
 * Interface for sending responses.
 * </p>
 *
 * @author prabath.
 */
public interface ResponseSender {

    void sendResponseBack(ByteBuffer buffer) throws WSFtpException;
}
