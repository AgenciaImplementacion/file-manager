package org.ut.entity;

import org.ut.util.FileTools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FolderInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<FileInfo> files;
    private List<FolderInfo> folders;
    private String path;
    private String name;
    private String connection;

    public FolderInfo() {
        this.files = new ArrayList<>();
        this.folders = new ArrayList<>();
        this.path = "";
    }

    public void addFile(FileInfo f) {
        this.files.add(f);
    }

    public void addFile(File f, String path) throws IOException {
        this.files.add(new FileInfo(f.getName(), path, f.length(), FileTools.getAttributesOfFile(f)));
    }

    public void addFolder(FolderInfo d) {
        this.folders.add(d);
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public List<FolderInfo> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderInfo> folders) {
        this.folders = folders;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

}
