package psaw.websocket.domain;

import java.util.Arrays;

/**
 * <p>
 * <code>FilePartDetail</code> -
 * Contains meta data and actual data for the file part.
 * </p>
 *
 * @author prabath.
 */
public class FilePartDetail {

    private long uniqueId;

    private int filePartIndex;

    private int filePartLength;

    private byte[] filePart;

    public long getUniqueId() {
        return uniqueId;
    }

    public int getFilePartIndex() {
        return filePartIndex;
    }

    public int getFilePartLength() {
        return filePartLength;
    }

    public byte[] getFilePart() {
        return filePart;
    }

    private FilePartDetail(Builder builder) {
        uniqueId = builder.uniqueId;
        filePartLength = builder.filePartLength;
        filePartIndex = builder.filePartIndex;
        filePart = builder.filePart;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {

        private long uniqueId;

        private int filePartLength;

        private int filePartIndex;

        private byte[] filePart;

        private Builder() {
        }

        public Builder withUniqueId(long uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public Builder withFilePartLength(int filePartLength) {
            this.filePartLength = filePartLength;
            return this;
        }

        public Builder withFilePartIndex(int filePartIndex) {
            this.filePartIndex = filePartIndex;
            return this;
        }

        public Builder withFilePart(byte[] filePart) {
            this.filePart = filePart;
            return this;
        }

        public FilePartDetail build() {
            return new FilePartDetail(this);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FilePartDetail{");
        sb.append("uniqueId='").append(uniqueId).append('\'');
        sb.append(", filePartIndex=").append(filePartIndex);
        sb.append(", filePartLength=").append(filePartLength);
        sb.append(", filePart=").append(Arrays.toString(filePart));
        sb.append('}');
        return sb.toString();
    }
}
