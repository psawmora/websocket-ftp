package psaw.websocket.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * <code>FileTransferSocketEndPoint</code> -
 * WebSocket endpoint for the charging requests. Used the native WS implementation.
 * </p>
 *
 * @author: prabath
 */

public class FileTransferSocketEndPoint extends WebSocketAdapter {

    private static final Logger logger = LogManager.getLogger(FileTransferSocketEndPoint.class);

    private int maxFilePartSize = 2 * 1024 * 1024;

    private Session session;

    private String defaultFilePath = "/home/prabath/Projects/websocket-ftp/core/src/main/resources/";

    //    private String defaultFilePath = "/software/The Maze Runner (2014)/The.Maze.Runner.2014.720p.BluRay.x264.YIFY.mp4";
    //    private String defaultFilePath = "/home/prabath/Projects/websocket-ftp/core/src/main/resources/call_busy.mp3";
    //    private String defaultFilePath = "/home/prabath/Projects/websocket-ftp/core/src/main/resources/test-text.txt";


    public FileTransferSocketEndPoint(ExecutorService senderService) {

    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        ByteBuffer requestBuffer = ByteBuffer.wrap(payload);
        byte[] nameLengthArray = new byte[4];
        requestBuffer = requestBuffer.get(nameLengthArray, 0, 4).slice();
        int nameLength = ByteBuffer.wrap(nameLengthArray).order(ByteOrder.LITTLE_ENDIAN).getInt();
        byte[] fileNameBytes = new byte[nameLength];
        requestBuffer.get(fileNameBytes, 0, nameLength);
        String fileName = new String(fileNameBytes, Charset.defaultCharset());
        System.out.println("File Name : " + fileName);
        String filePath = defaultFilePath.concat(fileName);
        readFile(filePath);
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("Got Message : " + message);
    }

    private void readFile(String filePath) {
        try (RandomAccessFile fileStream = new RandomAccessFile(new File(filePath), "r");
             FileChannel channel = fileStream.getChannel()) {
            ByteBuffer fileBuffer = ByteBuffer.allocate(maxFilePartSize);
            int length;
            int totalSize = 0;
            int index = 1;
            while ((length = channel.read(fileBuffer)) > 0) {
                sendFilePart(fileBuffer, length, index);
                fileBuffer.limit(maxFilePartSize).position(0);
                totalSize += length;
                index++;
                System.out.println("Total Size : " + totalSize);
            }
            session.getRemote().sendBytes(ByteBuffer.allocate(0));
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println("WebSocket Connection established");
        logger.info("WebSocket connection established [{}]", session);
        this.session = session;
    }

    @Override
    public void onWebSocketError(Throwable th) {
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
    }
}
