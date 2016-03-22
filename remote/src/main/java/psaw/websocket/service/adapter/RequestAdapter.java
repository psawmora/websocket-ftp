package psaw.websocket.service.adapter;

import psaw.websocket.domain.BasePdu;
import psaw.websocket.service.ResponseSender;
import psaw.websocket.service.WSFtpException;

import java.nio.ByteBuffer;

/**
 * <p>
 * <code>RequestAdapter</code> -
 * Transforms requests and responses to and from internal formats.
 * </p>
 *
 * @author prabath.
 */
public interface RequestAdapter<R extends BasePdu> {

    /**
     * <p>
     * Transform requests to an internal format from the received format.
     * </p>
     *
     * @param requestBuffer
     * @throws WSFtpException
     */
    void processRequest(ByteBuffer requestBuffer, String responseSenderId) throws WSFtpException;

    void register(String id, ResponseSender responseSender);

    void unRegister(String id);
}
