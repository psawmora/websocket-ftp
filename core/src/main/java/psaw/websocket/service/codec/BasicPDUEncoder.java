package psaw.websocket.service.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import psaw.websocket.domain.BasePdu;
import psaw.websocket.domain.DataPdu;
import psaw.websocket.domain.ServerAck;
import psaw.websocket.service.Status;
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

    private final static int FILE_ID_MAX_LENGTH = 8;

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

    private ByteBuffer encodeData(DataPdu pdu) throws WSFtpException {
        ByteBuf encodingBuffer = Unpooled.buffer();
        byte[] fileIdBuffer = encodeFileId(pdu.getId());
        ByteBuffer dataPart = pdu.getData().slice();
        return encodingBuffer
                .order(ByteOrder.LITTLE_ENDIAN)
                .writeByte(pdu.getType().getPduType())
                .writeBytes(fileIdBuffer)
                .writeInt(pdu.getPartLength())
                .writeBytes(dataPart).nioBuffer();
    }

    private ByteBuffer encodeAckServer(ServerAck pdu) throws WSFtpException {
        ByteBuf encodingBuffer = Unpooled.buffer();
        byte[] fileIdBuffer = encodeFileId(pdu.getId());
        encodingBuffer
                .order(ByteOrder.LITTLE_ENDIAN)
                .writeByte(pdu.getType().getPduType())
                .writeBytes(fileIdBuffer)
                .writeBoolean(pdu.isSentRequestAccepted());
        return encodingBuffer.nioBuffer();
    }

    private byte[] encodeFileId(String id) throws WSFtpException {
        byte[] originalBuffer = id.getBytes(Charset.defaultCharset());
        if (originalBuffer.length > FILE_ID_MAX_LENGTH) {
            throw new WSFtpException(Status.ENCODING_FAILURE, "File Id is longer that the max limit");
        }
        byte[] fileIdBuffer = new byte[FILE_ID_MAX_LENGTH];
        System.arraycopy(originalBuffer, 0, fileIdBuffer, 0, originalBuffer.length);
        return fileIdBuffer;
    }
}
