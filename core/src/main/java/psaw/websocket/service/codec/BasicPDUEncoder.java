package psaw.websocket.service.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import psaw.websocket.domain.FilePartDetail;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * <p>
 * <code>BasicPDUEncoder</code> -
 * The basic encoder for encoding PDUs.
 * </p>
 *
 * @author prabath.
 */
public class BasicPDUEncoder {

    public ByteBuffer encode(FilePartDetail filePartDetail) {
        ByteBuf encodingBuffer = Unpooled.buffer();
        encodingBuffer.order(ByteOrder.LITTLE_ENDIAN);
        encodingBuffer
                .setLong(0, filePartDetail.getUniqueId())
                .writeInt(filePartDetail.getFilePartLength())
                .writeInt(filePartDetail.getFilePartIndex())
                .writeBytes(filePartDetail.getFilePart());
        return encodingBuffer.nioBuffer();
    }
}
