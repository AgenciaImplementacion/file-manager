package org.ut.response;

import java.io.Serializable;

public class MessageResponse implements Serializable {

    private boolean status;
    private String message;

    public MessageResponse() {
        this.status = false;
        this.message = "";
    }

    public MessageResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setOk(String message) {
        this.message = message;
        this.setStatus(true);
    }

    public void setError(String message){
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
}
