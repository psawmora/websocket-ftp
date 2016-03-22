package psaw.websocket.domain;

import java.nio.ByteBuffer;

import static psaw.websocket.domain.Type.DATA;

/**
 * <p>
 * <code>DataPdu</code> -
 * Contains data pdu.
 * </p>
 *
 * @author prabath.
 */
public class DataPdu extends BasePdu {

    private int fileIndex;

    private long fileSize;

    private ByteBuffer data;

    private int partLength;

    public int getPartLength() {
        return partLength;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public long getFileSize() {
        return fileSize;
    }

    public ByteBuffer getData() {
        return data;
    }

    private DataPdu(Builder builder) {
        this.type = DATA;
        this.id = builder.id;
        this.responseSenderId = builder.responseSenderId;
        this.fileIndex = builder.fileIndex;
        this.fileSize = builder.fileSize;
        this.data = builder.data;
        this.partLength = builder.partLength;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private String id;

        private String responseSenderId;

        private int fileIndex;

        private long fileSize;

        private int partLength;

        private ByteBuffer data;

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

        public Builder withFileIndex(int fileIndex) {
            this.fileIndex = fileIndex;
            return this;
        }

        public Builder withFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder withPartLength(int partLength) {
            this.partLength = partLength;
            return this;
        }

        public Builder withData(ByteBuffer data) {
            this.data = data;
            return this;
        }

        public DataPdu build() {
            return new DataPdu(this);
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataPdu{");
        sb.append("super=").append(super.toString());
        sb.append("fileIndex=").append(fileIndex);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
