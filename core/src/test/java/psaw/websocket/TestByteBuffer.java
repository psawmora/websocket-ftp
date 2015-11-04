package psaw.websocket;

import java.nio.ByteBuffer;

/**
 * @author prabath.
 */
public class TestByteBuffer {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println(
                "Position : " + buffer.position() + " | Limit : " + buffer.limit() + " | Capacity : " + buffer.capacity());
        buffer.put((byte) 10);
        buffer.put((byte) 12);
        System.out.println(
                "Position : " + buffer.position() + " | Limit : " + buffer.limit() + " | Capacity : " + buffer.capacity());
        buffer.flip();
        buffer.rewind();
        System.out.println(
                "Position : " + buffer.position() + " | Limit : " + buffer.limit() + " | Capacity : " + buffer.capacity());
        ByteBuffer slicedBuffer = buffer.slice();
        System.out.println(
                "Position : " + slicedBuffer.position() + " | Limit : " + slicedBuffer.limit() + " | Capacity : " +
                        slicedBuffer.capacity());
        System.out.println(slicedBuffer.get(0));

    }
}
