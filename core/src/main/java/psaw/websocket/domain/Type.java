package psaw.websocket.domain;

/**
 * <p></p>
 *
 * @author prabath.
 */
public enum Type {
    INIT(1),
    ACK_FROM_SERVER(2),
    ACK_FROM_CLIENT(3),
    DATA(4),
    END(5);

    private final byte pduType;

    Type(int pduType) {
        this.pduType = (byte) pduType;
    }

    public byte getPduType() {
        return pduType;
    }
}
