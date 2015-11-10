package psaw.websocket.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.Servlet;
import java.util.Map;

/**
 * <p>
 * <code>JettyServer</code> -
 * Contains the code for the Jetty Server.
 * </p>
 *
 * @author prabath.
 */
public class JettyServer {

    private static final Logger LOGGER = LogManager.getLogger(JettyServer.class);

    private Map<String, Servlet> servlets;

    private int port = 9995;

    private byte corePoolSize = 10;

    private int maxPoolSize = 20;

    private int keepAliveTime = 5 * 60;

    public void init() {
        LOGGER.info("Initializing thread : Starting the Jetty server");
        Thread serverThread = new Thread(this::start);
        serverThread.setName("Jetty-Server-Start-Thread");
        serverThread.start();
    }

    private void start() {
        LOGGER.info("Starting Jetty server for configurations [{}]", this);
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool(corePoolSize, maxPoolSize, keepAliveTime);
        Server server = new Server(queuedThreadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(contextHandler);

        try {
            servlets.forEach((path, servlet) -> addToTheContainer(path, servlet, contextHandler));
            server.start();
            server.join();
        } catch (Throwable th) {
            LOGGER.error("Error occurred while starting the server.", th);
        }
    }

    private void addToTheContainer(String path, Servlet servlet, ServletContextHandler contextHandler) {
        ServletHolder holder = new ServletHolder("path : " + path, servlet);
        holder.setAsyncSupported(true);
        contextHandler.addServlet(holder, path);
    }

    public void setServlets(Map<String, Servlet> servlets) {
        this.servlets = servlets;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setCorePoolSize(byte corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
