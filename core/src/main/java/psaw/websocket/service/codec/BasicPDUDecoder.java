package psaw.websocket.service.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import psaw.websocket.domain.FileInitRequest;
import psaw.websocket.domain.FilePartAck;

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
public class BasicPDUDecoder {

    public FileInitRequest decodeFileInitRequest(ByteBuffer dataBuffer) {
        ByteBuf initRequestBuffer = Unpooled.wrappedBuffer(dataBuffer);
        initRequestBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int fileUriLength = initRequestBuffer.readInt();
        String fileUri = initRequestBuffer.toString(4, fileUriLength, Charset.forName("utf-8"));
        initRequestBuffer.readerIndex(initRequestBuffer.readerIndex() + fileUriLength);
        return FileInitRequest.newBuilder().withFilePath(fileUri).build();
    }

    public FilePartAck decodeFilePartAck(ByteBuffer dataBuffer) {
        ByteBuf initRequestBuffer = Unpooled.wrappedBuffer(dataBuffer);
        initRequestBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long uniqueId = initRequestBuffer.readLong();
        int nextFilePartIndex = initRequestBuffer.readInt();
        return FilePartAck.newBuilder()
                .withFilePartIndex(nextFilePartIndex)
                .withUniqueId(uniqueId).build();
    }

}
