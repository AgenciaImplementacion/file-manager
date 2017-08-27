package org.ut.entity;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String name;
    private String path;
    private long size;


    public FileInfo(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }
}
