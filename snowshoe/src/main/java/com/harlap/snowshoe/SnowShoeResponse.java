package com.harlap.snowshoe;

import java.util.Date;

public class SnowShoeResponse {

    private Stamp stamp;
    private String receipt;
    private boolean secure;
    private Error error;
    private Date created;

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public class Stamp {
        private String serial;

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }
    }

    public class Error {
        private String message;
        private int code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("SnowShoeResponse: ")
                .append(" receipt: ").append(receipt)
                .append(" created: ").append(created);
        if (stamp != null) builder.append("stamp: ").append(stamp.serial);
        if (error != null) builder.append(" error: ").append(error.message);
        return builder.toString();
    }
}
