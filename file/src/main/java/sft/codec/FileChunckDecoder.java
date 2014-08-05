package sft.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import sft.bean.FileChunk;

/**
 * <p>
 * Decoder of the FileChunk. Implement this for different types of decoding mechanisms.
 * </p>
 *
 * @author: prabath
 */
public interface FileChunckDecoder {

    FileChunk decode(ChannelBuffer encodedData);
}
