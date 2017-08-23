package org.ut.entity;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String name;
    private String url;
    private long size;


    public FileInfo(String name, String url, long size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }
}
