package psaw.websocket.service;

/**
 * <p>
 * <code>WSFtpException</code> -
 * Internal exception class.
 * </p>
 *
 * @author prabath.
 */
public class WSFtpException extends Exception {

    private Status status;

    public WSFtpException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public WSFtpException(Status status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WSFtpException{");
        sb.append(super.toString());
        sb.append("status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
