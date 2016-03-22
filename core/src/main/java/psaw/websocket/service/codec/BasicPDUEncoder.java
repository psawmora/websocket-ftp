package psaw.websocket.service.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import psaw.websocket.domain.BasePdu;
import psaw.websocket.domain.DataPdu;
import psaw.websocket.domain.ServerAck;
import psaw.websocket.service.WSFtpException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * <p>
 * <code>BasicPDUEncoder</code> -
 * The basic encoder for encoding PDUs.
 * </p>
 *
 * @author prabath.
 */
public class BasicPDUEncoder implements PduEncoder<BasePdu> {

    @Override
    public ByteBuffer encode(BasePdu pdu) throws WSFtpException {
        switch (pdu.getType()) {
            case ACK_FROM_SERVER:
                return encodeAckServer((ServerAck) pdu);
            case DATA:
                return encodeData((DataPdu) pdu);
        }
        return null;
    }

    private ByteBuffer encodeData(DataPdu pdu) {
        ByteBuf encodingBuffer = Unpooled.buffer();
        String fileId = pdu.getId();
        ByteBuffer dataPart = pdu.getData().slice();
        return encodingBuffer
                .order(ByteOrder.LITTLE_ENDIAN)
                .writeByte(pdu.getType().getPduType())
                .writeInt(fileId.getBytes().length)
                .writeBytes(fileId.getBytes(Charset.defaultCharset()))
                .writeInt(pdu.getPartLength())
                .writeBytes(dataPart).nioBuffer();
    }

    private ByteBuffer encodeAckServer(ServerAck pdu) {
        ByteBuf encodingBuffer = Unpooled.buffer();
        String fileId = pdu.getId();
        encodingBuffer
                .order(ByteOrder.LITTLE_ENDIAN)
                .writeByte(pdu.getType().getPduType())
                .writeInt(fileId.getBytes().length)
                .writeBytes(fileId.getBytes(Charset.defaultCharset()))
                .writeBoolean(pdu.isSentRequestAccepted());
        return encodingBuffer.nioBuffer();
    }
}
