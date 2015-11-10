package psaw.websocket.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * <code>FileWebSocketServlet</code> -
 * Servlet that responds to WS promote requests.
 * </p>
 *
 * @author: prabath
 */
public class FileWebSocketServlet extends WebSocketServlet {

    private static final Logger logger = LogManager.getLogger(FileWebSocketServlet.class);

    private ExecutorService senderService;

    private int wsEndPointSenderThreadCount = 10;

    @Override
    public void log(String msg) {
        logger.debug("WebSocket Servlet Log [{}]", msg);
        super.log(msg);
    }

    @Override
    public void log(String message, Throwable th) {
        logger.error("WebSocket Servlet Error Log [{}]", message, th);
        super.log(message, th);
    }

    @Override
    public void init() throws ServletException {
        try {
            logger.info("Initializing the WebSocket Servlet");
            senderService = Executors.newFixedThreadPool(wsEndPointSenderThreadCount);
            super.init();
        } catch (ServletException e) {
            logger.error("Error occurred while initializing the WebSocket Servlet", e);
            throw e;
        }
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        if (senderService == null) {
            senderService = Executors.newFixedThreadPool(wsEndPointSenderThreadCount);
        }
        logger.info("Configuring WebSocket Endpoint..");
        webSocketServletFactory.setCreator(
                (servletUpgradeRequest, servletUpgradeResponse) -> {
                    logger.debug("New WebSocket Upgrade Request Received. [{}]", servletUpgradeRequest);
                    return new FileTransferSocketEndPoint(senderService);
                });
    }
}
