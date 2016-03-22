package psaw.websocket.service.codec;

import psaw.websocket.domain.BasePdu;
import psaw.websocket.domain.ClientAck;
import psaw.websocket.domain.InitPdu;
import psaw.websocket.domain.Type;
import psaw.websocket.service.WSFtpException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * <p>
 * <code>BasicPDUDecoder</code> -
 * The basic decoder for decoding PDUs.
 * </p>
 *
 * @author prabath.
 */
public class BasicPDUDecoder implements PduDecoder<BasePdu> {

    @Override
    public BasePdu decode(ByteBuffer requestBuffer) throws WSFtpException {
        requestBuffer = requestBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte type = requestBuffer.get();
        Type pduType = getType(type);
        if (pduType != null) {
            return decodeByType(requestBuffer, pduType);
        }
        return null;
    }

    private BasePdu decodeByType(ByteBuffer requestBuffer, Type pduType) {
        switch (pduType) {
            case INIT:
                return decodeInit(requestBuffer);
            case ACK_FROM_CLIENT:
                return decodeClientAck(requestBuffer);
        }
        return null;
    }

    private BasePdu decodeClientAck(ByteBuffer requestBuffer) {
        int byteLengthId = requestBuffer.getInt();
        byte[] idBuffer = new byte[byteLengthId];
        requestBuffer.get(idBuffer);
        String id = new String(idBuffer, Charset.defaultCharset());
        int nextIndex = requestBuffer.getInt();
        return ClientAck.newBuilder()
                .withId(id)
                .withNextFilePart(nextIndex)
                .build();
    }

    private BasePdu decodeInit(ByteBuffer requestBuffer) {
        int byteLengthId = requestBuffer.getInt();
        byte[] idBuffer = new byte[byteLengthId];
        requestBuffer.get(idBuffer);
        String id = new String(idBuffer, Charset.defaultCharset());
        int byteLengthFileName = requestBuffer.getInt();
        byte[] nameBuffer = new byte[byteLengthFileName];
        requestBuffer.get(nameBuffer);
        String fileName = new String(nameBuffer, Charset.defaultCharset());
        return InitPdu.newBuilder()
                .withFileName(fileName)
                .withId(id)
                .build();
    }

    private Type getType(byte type) {
        for (Type pduType : Type.values()) {
            if (pduType.getPduType() == type) {
                return pduType;
            }
        }
        return null;
    }
}
