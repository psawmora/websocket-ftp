package psaw.websocket.domain;

import static psaw.websocket.domain.Type.INIT;

/**
 * <p></p>
 *
 * @author prabath.
 */
public class InitPdu extends BasePdu {

    private String fileName;

    private String filePath;

    private InitPdu(Builder builder) {
        setId(builder.id);
        this.type = INIT;
        responseSenderId = builder.responseSenderId;
        setFileName(builder.fileName);
        setFilePath(builder.filePath);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static final class Builder {

        private String id;

        private String responseSenderId;

        private String fileName;

        private String filePath;

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

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public InitPdu build() {
            return new InitPdu(this);
        }
    }
}
