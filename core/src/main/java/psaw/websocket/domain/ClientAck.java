package psaw.websocket.domain;

/**
 * <p>
 * <code>ClientAck</code> -
 * Ack comes from client side.
 * </p>
 *
 * @author prabath.
 */
public class ClientAck extends BasePdu {

    private int nextFilePart;

    private ClientAck(Builder builder) {
        this.type = Type.ACK_FROM_CLIENT;
        id = builder.id;
        setResponseSenderId(builder.responseSenderId);
        nextFilePart = builder.nextFilePart;
    }

    public int getNextFilePart() {
        return nextFilePart;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {

        private String id;

        private String responseSenderId;

        private int nextFilePart;

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

        public Builder withNextFilePart(int nextFilePart) {
            this.nextFilePart = nextFilePart;
            return this;
        }

        public ClientAck build() {
            return new ClientAck(this);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientAck{");
        sb.append("nextFilePart=").append(nextFilePart);
        sb.append('}');
        return sb.toString();
    }
}
