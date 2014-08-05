package sft.codec;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * <p>
 *     Encoder of the FileChunk. Implement this for different types of encoding mechanisms.
 * </p>
 * @author: prabath
 */
public interface FileChunckEncoder {
  ChannelBuffer encode();
}
