package psaw.websocket.service.codec;

import psaw.websocket.domain.BasePdu;
import psaw.websocket.service.WSFtpException;

import java.nio.ByteBuffer;

/**
 * <p>
 * <code>PduDecoder</code> -
 * Interface for decoding requests.
 * </p>
 *
 * @author prabath.
 */
public interface PduDecoder<R extends BasePdu> {

    R decode(ByteBuffer requestBuffer) throws WSFtpException;
}
