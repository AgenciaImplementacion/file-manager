package org.ut.response;

import org.ut.entity.FolderInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FolderListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean status;
    private String message;
    private List<FolderInfo> data;

    public FolderListResponse() {
        this.status = false;
        this.message = "";
        this.data = new ArrayList<>();
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

    public List<FolderInfo> getData() {
        return data;
    }

    public void setData(List<FolderInfo> data) {
        this.data = data;
    }

    public void addData(FolderInfo data) {
        this.data.add(data);
    }
}
