package sft;

import com.sun.nio.sctp.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author: prabath
 */
public class SimpleSftpServerNClient {

    public static final int SERVER_PORT = 50113;

    public static final int STREAM_ONE = 1;

    public static final int STREAM_TWO = 2;

    public static final int MAX_SEND_SIZE = 102400; // 10kb

    public void initSctpServer() throws IOException {
        SctpServerChannel sctpServerChannel = SctpServerChannel.open();
        sctpServerChannel.configureBlocking(false);
        sctpServerChannel.bind(new InetSocketAddress(SERVER_PORT));

        while (true) {
            SctpChannel sctpCh = sctpServerChannel.accept();
            if (sctpCh != null) {
                System.out.println("Got a Sctp Connection");
                SctpServerRunnable sctpServerRunnable = new SctpServerRunnable(sctpCh);
                Thread th = new Thread(sctpServerRunnable);
                th.start();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

        }
    }

    private static class SctpServerRunnable implements Runnable {

        private SctpChannel channel;

        public SctpServerRunnable(SctpChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            ByteBuffer receiveBuff = ByteBuffer.allocate(10);
            MessageInfo msgInfo = null;
            while (true) {
                try {
                    msgInfo = channel.receive(receiveBuff, System.out, new NotificationHandler<PrintStream>() {
                        @Override
                        public HandlerResult handleNotification(Notification notification, PrintStream printStream) {
                            return null;
                        }
                    });
                    if (msgInfo != null) {
                        System.out.println("SCTP Message : " + new String(receiveBuff.array()) + " Stream Number  : " +
                                msgInfo.streamNumber());
                    }
                    receiveBuff.clear();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void startSctpClient() throws IOException {
        SctpChannel channel = SctpChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", SERVER_PORT));
        while (!channel.finishConnect()) {
            try {
                System.out.println("Trying to connect");
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        ByteBuffer writeBuff = ByteBuffer.wrap(new String("Test1Test1").getBytes());
        channel.send(writeBuff, MessageInfo.createOutgoing(null, STREAM_ONE));
        channel.send((ByteBuffer) writeBuff.flip(), MessageInfo.createOutgoing(null, STREAM_TWO));
        channel.send((ByteBuffer) writeBuff.flip(), MessageInfo.createOutgoing(null, STREAM_ONE));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.close();
    }

    public static void main(String[] args) {
        final SimpleSftpServerNClient serverNClient = new SimpleSftpServerNClient();
        try {
            Thread th1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverNClient.initSctpServer();
                    } catch (Throwable e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            });
            Thread.sleep(100);
            Thread th2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverNClient.startSctpClient();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            th1.start();
            th2.start();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
