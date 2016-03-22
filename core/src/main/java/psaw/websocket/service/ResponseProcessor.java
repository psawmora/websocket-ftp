package psaw.websocket.service;

import psaw.websocket.domain.BasePdu;

/**
 * <p>
 * <code>ResponseProcessor</code>  -
 * Process responses.
 * </p>
 *
 * @author prabath.
 */
public interface ResponseProcessor<R extends BasePdu> {

    void processResponse(R pdu) throws WSFtpException;
}
