package psaw.websocket;

import psaw.websocket.server.JettyServer;
import psaw.websocket.service.FileWebSocketServlet;

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
        Map<String, Servlet> servlets = new HashMap<>();
        servlets.put("/file", webSocketServlet);
        JettyServer jettyServer = new JettyServer();
        jettyServer.setServlets(servlets);
        jettyServer.init();
    }
}
