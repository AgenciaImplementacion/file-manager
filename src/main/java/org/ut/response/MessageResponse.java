package org.ut.response;

import java.io.Serializable;

public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean status;
    private String message;
    private String url = "";

    public MessageResponse() {
        this.status = false;
        this.message = "";
    }

    public void setOk(String message) {
        this.message = message;
        this.setStatus(true);
    }

    public void setError(String message) {
        this.message = message;
        this.setStatus(false);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the id
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param id the id to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
