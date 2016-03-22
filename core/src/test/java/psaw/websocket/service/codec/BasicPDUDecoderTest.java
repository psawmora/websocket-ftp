package psaw.websocket.service.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * <p>
 * <code>BasicPDUDecoderTest</code> -
 * Unit tests.
 * </p>
 *
 * @author prabath.
 */
public class BasicPDUDecoderTest {

    @Test
    public void testFileInitRequestDecode() {
        BasicPDUDecoder pduDecoder = new BasicPDUDecoder();
        String filePath = "Test-File-Path-1/test_file.txt";
        byte[] filePathBuffer = filePath.getBytes(Charset.forName("utf-8"));

        ByteBuf encodingBuffer = Unpooled.buffer();
        encodingBuffer.order(ByteOrder.LITTLE_ENDIAN);
        encodingBuffer
                .writeInt(filePathBuffer.length)
                .writeBytes(filePathBuffer);
        //        FileInitRequest fileInitRequest = pduDecoder.decodeFileInitRequest(encodingBuffer.nioBuffer());
        //        System.out.println(fileInitRequest);

    }

    @Test
    public void testFilePartAckDecode() {
        BasicPDUDecoder pduDecoder = new BasicPDUDecoder();

        ByteBuf encodingBuffer = Unpooled.buffer();
        encodingBuffer.order(ByteOrder.LITTLE_ENDIAN);
        encodingBuffer
                .writeLong(Long.parseLong("1122222112121211111"))
                .writeInt(10);
        //        ServerAck serverAck = pduDecoder.decodeFilePartAck(encodingBuffer.nioBuffer());
        //        System.out.println(serverAck);

    }
}