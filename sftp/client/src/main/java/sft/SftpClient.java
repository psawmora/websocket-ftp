package sft;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * <p>
 *     Sftp client interface. Implement this for new clients.
 * </p>
 * @author: prabath
 */
public interface SftpClient {
    void initConnection(ConnectionDetail detail)
    void send(ChannelBuffer encodedData);

}
