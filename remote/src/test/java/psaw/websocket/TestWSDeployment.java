package psaw.websocket;

import psaw.websocket.server.JettyServer;
import psaw.websocket.service.DefaultFileRequestHandler;
import psaw.websocket.service.DefaultRequestAdapter;
import psaw.websocket.service.FileWebSocketServlet;
import psaw.websocket.service.codec.BasicPDUDecoder;
import psaw.websocket.service.codec.BasicPDUEncoder;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <code>TestWSDeployment</code> -
 * Deploying the Jetty server for testing.
 * </p>
 *
 * @author prabath.
 */
public class TestWSDeployment {

    public static void main(String[] args) {
        TestWSDeployment testWSDeployment = new TestWSDeployment();
        testWSDeployment.initiateServer();
    }

    private void initiateServer() {
        FileWebSocketServlet webSocketServlet = new FileWebSocketServlet();

        DefaultRequestAdapter requestAdapter = new DefaultRequestAdapter();
        requestAdapter.setDecoder(new BasicPDUDecoder());
        requestAdapter.setEncoder(new BasicPDUEncoder());

        DefaultFileRequestHandler fileRequestHandler = new DefaultFileRequestHandler();
        fileRequestHandler.setResponseProcessor(requestAdapter);
        fileRequestHandler.init();
        requestAdapter.setFileRequestHandler(fileRequestHandler);
        requestAdapter.init();

        webSocketServlet.setRequestAdapter(requestAdapter);

        Map<String, Servlet> servlets = new HashMap<>();
        servlets.put("/file", webSocketServlet);
        JettyServer jettyServer = new JettyServer();
        jettyServer.setServlets(servlets);
        jettyServer.init();
    }
}
