package org.ut.entity;

import java.io.Serializable;
import java.util.Map;

public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String path;
    private long size;
    private Map<String, String> attributes;

    public FileInfo(String name, String path, long size, Map<String, String> attributes) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.attributes = attributes;
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

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
