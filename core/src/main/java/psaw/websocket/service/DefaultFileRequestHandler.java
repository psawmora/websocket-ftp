package psaw.websocket.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import psaw.websocket.domain.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * <code>DefaultFileRequestHandler</code> -
 * Accepts requests from remote file clients and sends processed responses.
 * </p>
 *
 * @author prabath.
 */
public class DefaultFileRequestHandler implements FileRequestHandler<BasePdu> {

    private static final Logger logger = LogManager.getLogger(DefaultFileRequestHandler.class);

    private String basePath = "/home/prabath/Projects/websocket-ftp/core/src/main/resources/";

    private int defaultBufferSize = 1024 * 1024;

    private ResponseProcessor<BasePdu> responseProcessor;

    private ExecutorService responseSenderService;

    private int senderThreadCount = 2;

    private Map<String, FileDetail> fileDetails;

    public void init() {
        logger.info("Initializing DefaultFileRequestHandler", this);
        fileDetails = new ConcurrentHashMap<>();
        responseSenderService = Executors.newFixedThreadPool(senderThreadCount);
    }

    @Override
    public void handleRequest(BasePdu pdu) throws WSFtpException {
        logger.debug("Handling the requests [{}]", pdu);
        Type type = pdu.getType();

        switch (type) {
            case INIT:
                handleInit((InitPdu) pdu);
                break;
            case ACK_FROM_CLIENT:
                handleClientAck((ClientAck) pdu);
                break;
            default:
                logger.debug("The PDU type [{}] cannot be handled", type);
        }
    }

    private void handleClientAck(ClientAck pdu) {
        String id = pdu.getId();
        FileDetail fileDetail = fileDetails.get(id);
        if (fileDetail != null) {
            sendFilePart(fileDetail, pdu);
        }
    }

    private void sendFilePart(FileDetail fileDetail, ClientAck pdu) {
        Path filePath = Paths.get(basePath, fileDetail.fileName);
        FilePartDetail filePartDetail = readFileByPart(pdu.getNextFilePart(), filePath.toString());
        DataPdu dataPdu = DataPdu.newBuilder()
                .withData(ByteBuffer.wrap(filePartDetail.buffer))
                .withId(pdu.getId())
                .withFileIndex(pdu.getNextFilePart())
                .withFileSize(filePartDetail.length)
                .withPartLength(filePartDetail.length)
                .withResponseSenderId(pdu.getResponseSenderId()).build();
        responseSenderService.execute(() -> {
            try {
                responseProcessor.processResponse(dataPdu);
            } catch (WSFtpException e) {
                logger.error("Error occurred while sending the Data PDU", e);
            }
        });
    }

    private void handleInit(InitPdu pdu) {
        String fileName = pdu.getFileName();
        File file = new File(basePath, fileName);
        ServerAck serverAck = ServerAck.newBuilder()
                .withId(pdu.getId())
                .withIsSentRequestAccepted(file.exists())
                .build();
        serverAck.setResponseSenderId(pdu.getResponseSenderId());
        fileDetails.putIfAbsent(pdu.getId(), new FileDetail(fileName));
        responseSenderService.execute(() -> {
            try {
                responseProcessor.processResponse(serverAck);
            } catch (WSFtpException e) {
                logger.error("Error occurred while sending the ServerAck PDU", e);
            }
        });
    }

    private FilePartDetail readFileByPart(int filePart, String path) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r")) {
            byte[] buffer = new byte[defaultBufferSize];
            long length = randomAccessFile.length();
            long offset = filePart == 0 ? 0 : (long) filePart * defaultBufferSize;
            randomAccessFile.seek(offset);
            int nReadBytes = randomAccessFile.read(buffer);
            return new FilePartDetail(buffer, nReadBytes);
        } catch (Exception e) {
            logger.error("Error occurred while reading the file part", e);
        }
        return null;
    }

    private static class FileDetail {

        String fileName;

        public FileDetail(String fileName) {
            this.fileName = fileName;
        }
    }

    private static class FilePartDetail {

        byte[] buffer;

        int length;

        public FilePartDetail(byte[] buffer, int length) {
            this.buffer = buffer;
            this.length = length < 0 ? 0 : length;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("FilePartDetail{");
            sb.append("length=").append(length);
            sb.append('}');
            return sb.toString();
        }
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setDefaultBufferSize(int defaultBufferSize) {
        this.defaultBufferSize = defaultBufferSize;
    }

    public void setResponseProcessor(ResponseProcessor<BasePdu> responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    public void setSenderThreadCount(int senderThreadCount) {
        this.senderThreadCount = senderThreadCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultFileRequestHandler{");
        sb.append("basePath='").append(basePath).append('\'');
        sb.append(", defaultBufferSize=").append(defaultBufferSize);
        sb.append('}');
        return sb.toString();
    }
}
