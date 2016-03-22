package psaw.websocket.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import psaw.websocket.domain.BasePdu;
import psaw.websocket.service.adapter.RequestAdapter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * <code>FileTransferSocketEndPoint</code> -
 * WebSocket endpoint for the charging requests. Used the native WS implementation.
 * </p>
 *
 * @author: prabath
 */

public class FileTransferSocketEndPoint extends WebSocketAdapter implements ResponseSender {

    private static final Logger logger = LogManager.getLogger(FileTransferSocketEndPoint.class);

    private final String id;

    private int maxFilePartSize = 2 * 1024 * 1024;

    private Session session;

    private RequestAdapter<BasePdu> requestAdapter;

    public FileTransferSocketEndPoint(ExecutorService senderService, RequestAdapter requestAdapter) {
        this.requestAdapter = requestAdapter;
        this.id = String.valueOf(System.currentTimeMillis());
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        ByteBuffer requestBuffer = ByteBuffer.wrap(payload);
        try {
            requestAdapter.processRequest(requestBuffer, id);
        } catch (Exception e) {
            logger.error("Error occurred while processing the request", e);
        }
    }

    @Override
    public void sendResponseBack(ByteBuffer buffer) throws WSFtpException {
        try {
            session.getRemote().sendBytesByFuture(buffer);
        } catch (Exception e) {
            throw new WSFtpException(Status.RESPONSE_SEND_FAILED, "Response sending failed", e);
        }
    }

    /**
     * <p>
     * Sends a part of the file.
     * The PDU format :
     * |Length_of_the_file_part(4Bytes)|part_index(4Bytes)|file_part|
     * </p>
     *
     * @param fileBuffer
     * @param length
     * @param index
     * @throws IOException
     */
    private void sendFilePart(ByteBuffer fileBuffer, int length, int index) throws IOException {
        System.out.println("Read Length : " + length + " | Index : " + index);
        ByteBuffer filePartPdu = ByteBuffer.allocate(length + 8);
        filePartPdu.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer rawFilePartDataBuffer = ((ByteBuffer) fileBuffer.flip()).slice();
        filePartPdu.putInt(length).putInt(index).put(rawFilePartDataBuffer);
        session.getRemote().sendBytes((ByteBuffer) filePartPdu.slice().rewind());
    }

    @Override
    public void onWebSocketConnect(Session session) {
        logger.info("WebSocket connection established [{}]", session);
        this.session = session;
        requestAdapter.register(id, this);
    }

    @Override
    public void onWebSocketError(Throwable th) {
        requestAdapter.unRegister(id);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        requestAdapter.unRegister(id);
    }

    @Override
    public void onWebSocketText(String message) {
        logger.debug("Got Text Message : [{}]", message);
    }
}
