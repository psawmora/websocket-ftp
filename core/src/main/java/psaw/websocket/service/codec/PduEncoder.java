package psaw.websocket.service.codec;

import psaw.websocket.domain.BasePdu;
import psaw.websocket.service.WSFtpException;

import java.nio.ByteBuffer;

/**
 * <p>
 * <code>PduEncoder</code> -
 * Interface for encoding PDUs.
 * </p>
 *
 * @author prabath.
 */
public interface PduEncoder<R extends BasePdu> {

    ByteBuffer encode(R pdu) throws WSFtpException;
}
