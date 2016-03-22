package psaw.websocket.domain;

/**
 * <p>
 * <code>BasePdu</code> -
 * Base Pdu.
 * </p>
 *
 * @author prabath.
 */
public class BasePdu {

    protected String id;

    protected Type type;

    protected String responseSenderId;

    public Type getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getResponseSenderId() {
        return responseSenderId;
    }

    public void setResponseSenderId(String responseSenderId) {
        this.responseSenderId = responseSenderId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BasePdu{");
        sb.append("type=").append(type);
        sb.append(", id='").append(id).append('\'');
        sb.append(", responseSenderId='").append(responseSenderId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
