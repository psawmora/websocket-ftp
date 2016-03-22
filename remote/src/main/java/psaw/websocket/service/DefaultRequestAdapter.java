package psaw.websocket.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import psaw.websocket.domain.BasePdu;
import psaw.websocket.service.adapter.RequestAdapter;
import psaw.websocket.service.codec.PduDecoder;
import psaw.websocket.service.codec.PduEncoder;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <code>DefaultRequestAdapter</code> -
 * Transforms requests and response from and to the underline transport protocol format.
 * </p>
 *
 * @author prabath.
 */
public class DefaultRequestAdapter<R extends BasePdu> implements RequestAdapter<R>, ResponseProcessor<R> {

    private static final Logger logger = LogManager.getLogger(DefaultRequestAdapter.class);

    private PduDecoder<BasePdu> decoder;

    private PduEncoder<BasePdu> encoder;

    private Map<String, ResponseSender> responseSenders;

    private FileRequestHandler<BasePdu> fileRequestHandler;

    private ResponseSender responseSender;

    public void init() {
        responseSenders = new ConcurrentHashMap<>();
    }

    @Override
    public void register(String id, ResponseSender responseSender) {
        logger.info("Registering response sender for the ID [{}]", id);
        responseSenders.put(id, responseSender);
    }

    @Override
    public void unRegister(String id) {
        logger.info("UnRegistering response sender for the ID [{}]", id);
        responseSenders.remove(id);
    }

    @Override
    public void processRequest(ByteBuffer requestBuffer, String responseSenderId) throws WSFtpException {
        logger.debug("Requests received to be processed from the id [{}]", responseSenderId);
        BasePdu pdu = decoder.decode(requestBuffer);
        pdu.setResponseSenderId(responseSenderId);
        logger.debug("PDU : [{}]", pdu);
        fileRequestHandler.handleRequest(pdu);
    }

    @Override
    public void processResponse(R responsePdu) throws WSFtpException {
        logger.debug("Processing the response PDU");
        ByteBuffer responseBuffer = encoder.encode(responsePdu);
        String responseSenderId = responsePdu.getResponseSenderId();
        ResponseSender responseSender = responseSenders.get(responseSenderId);
        if (responseSender != null) {
            responseSender.sendResponseBack(responseBuffer);
        }
    }

    public void setDecoder(PduDecoder<BasePdu> decoder) {
        this.decoder = decoder;
    }

    public void setEncoder(PduEncoder<BasePdu> encoder) {
        this.encoder = encoder;
    }

    public void setFileRequestHandler(FileRequestHandler<BasePdu> fileRequestHandler) {
        this.fileRequestHandler = fileRequestHandler;
    }
}
