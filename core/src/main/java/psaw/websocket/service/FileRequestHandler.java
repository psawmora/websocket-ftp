package psaw.websocket.service;

import psaw.websocket.domain.BasePdu;

/**
 * <p>
 * <code>FileRequestHandler</code> -
 * Interface for handling requests for files.
 * </p>
 *
 * @author prabath.
 */
public interface FileRequestHandler<R extends BasePdu> {

    void handleRequest(R pdu) throws WSFtpException;
}
