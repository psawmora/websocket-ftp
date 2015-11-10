package psaw.websocket.domain;

/**
 * <p>
 * <code>FilePartAck</code> -
 * Contains file part ack PDU details.
 * </p>
 *
 * @author prabath.
 */
public class FilePartAck {

    private long uniqueId;

    private int filePartIndex;

    public long getUniqueId() {
        return uniqueId;
    }

    public int getFilePartIndex() {
        return filePartIndex;
    }

    private FilePartAck(Builder builder) {
        uniqueId = builder.uniqueId;
        filePartIndex = builder.filePartIndex;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {

        private long uniqueId;

        private int filePartIndex;

        private Builder() {
        }

        public Builder withUniqueId(long uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public Builder withFilePartIndex(int filePartIndex) {
            this.filePartIndex = filePartIndex;
            return this;
        }

        public FilePartAck build() {
            return new FilePartAck(this);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FilePartAck{");
        sb.append("uniqueId='").append(uniqueId).append('\'');
        sb.append(", filePartIndex=").append(filePartIndex);
        sb.append('}');
        return sb.toString();
    }
}
