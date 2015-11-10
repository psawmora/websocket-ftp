package psaw.websocket.domain;

/**
 * <p>
 * <code>FileInitRequest</code> -
 * Contains initial PDU details.
 * </p>
 *
 * @author prabath.
 */
public class FileInitRequest {

    private String filePath;

    private FileInitRequest(Builder builder) {
        filePath = builder.filePath;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getFilePath() {
        return filePath;
    }


    public static final class Builder {

        private String filePath;

        private Builder() {
        }

        public Builder withFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public FileInitRequest build() {
            return new FileInitRequest(this);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileInitRequest{");
        sb.append("filePath='").append(filePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
