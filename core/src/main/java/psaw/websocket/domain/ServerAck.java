package psaw.websocket.domain;

/**
 * <p>
 * <code>ServerAck</code> -
 * Ack sent from the server to the client. Sent as a response for the INIT request.
 * </p>
 *
 * @author prabath.
 */
public class ServerAck extends BasePdu {

    private boolean isSentRequestAccepted;

    private ServerAck(Builder builder) {
        this.type = Type.ACK_FROM_SERVER;
        id = builder.id;
        responseSenderId = builder.responseSenderId;
        isSentRequestAccepted = builder.isSentRequestAccepted;
    }

    public boolean isSentRequestAccepted() {
        return isSentRequestAccepted;
    }

    public void setIsSentRequestAccepted(boolean isSentRequestAccepted) {
        this.isSentRequestAccepted = isSentRequestAccepted;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {

        private String id;

        private String responseSenderId;

        private boolean isSentRequestAccepted;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withResponseSenderId(String responseSenderId) {
            this.responseSenderId = responseSenderId;
            return this;
        }

        public Builder withIsSentRequestAccepted(boolean isSentRequestAccepted) {
            this.isSentRequestAccepted = isSentRequestAccepted;
            return this;
        }

        public ServerAck build() {
            return new ServerAck(this);
        }
    }
}
