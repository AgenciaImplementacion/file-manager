package org.ut.driver;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import org.ut.entity.FolderInfo;
import org.ut.util.FileTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
// import java.util.logging.Logger;

public class DLocalFiles implements Driver {

    // private final static Logger LOGGER =
    // Logger.getLogger(DLocalFiles.class.getName());

    private Properties config;
    private String fullBasePath;

    public DLocalFiles(Properties config) {
        this.setConfig(config);
        this.fullBasePath = config.getProperty("path");
    }

    @Override
    public void setConfig(Properties config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return this.config.getProperty("name");
    }

    public boolean store(MultipartFile file, String name, String path, boolean rewrite) throws IOException {
        if (!file.isEmpty()) {
            FileTools.saveFile(file, name, this.fullBasePath + File.separatorChar + path, rewrite);
            FileTools.generateThumbnail(file,
                    this.fullBasePath + File.separatorChar + path + File.separatorChar + name);
            return true;
        }
        return false;
    }

    @Override
    public FolderInfo list(String path, int depth) throws IOException {
        FolderInfo f = new FolderInfo();
        File[] files = FileTools.getFilesFolder(FilenameUtils.normalize(this.fullBasePath + File.separatorChar + path));
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (depth > 0)
                        depth -= 1;
                    if (depth != 0)
                        f.addFolder(this.list(path + File.separatorChar + file.getName(), depth));
                    else {
                        FolderInfo tmp = new FolderInfo();
                        tmp.setName(file.getName());
                        tmp.setConnection(this.getName());
                        tmp.setPath(path + File.separatorChar + file.getName());
                        f.addFolder(tmp);
                    }
                } else {
                    f.addFile(file, path);
                }
            }
        }
        f.setPath(path);
        String name = FilenameUtils.getBaseName(path);
        f.setName(name.isEmpty() ? "/" : name);
        f.setConnection(this.getName());
        return f;
    }

    @Override
    public boolean isFile(String path) throws IOException {
        return FileTools.isFile(this.fullBasePath + File.separatorChar + path + ".zip");
    }

    @Override
    public String getFullPath() {
        return this.fullBasePath;
    }

    @Override
    public Map<String, Object> getFile(String path, Boolean thumbnail, String size) throws IOException {
        Map<String, Object> response = new HashMap<>();
        String ext = ".zip";
        byte[] content = new byte[0];
        String fname = "";
        if (thumbnail) {
            File f = new File(this.fullBasePath + path + size + ".png");
            if (!f.isFile()) {
                f = new File(this.fullBasePath + path + ".zip");
                if (f.isFile()) {
                    FileSystemResource file = new FileSystemResource(this.fullBasePath + path + ".zip");
                    HashMap<String, byte[]> unzipfiles = FileTools.unZipIt(file.getInputStream());
                    if (unzipfiles.size() == 1) {
                        for (String i : unzipfiles.keySet()) {
                            ext = FilenameUtils.getExtension(i);
                            f = new File(this.fullBasePath + "/assets/" + ext + ".png");
                            if (!f.isFile()) {
                                f = new File(this.fullBasePath + "/assets/any.png");
                            }
                            break;
                        }
                    } else {
                        f = new File(this.fullBasePath + "/assets/zip.png");
                    }
                } else {
                    f = new File(this.fullBasePath + "/assets/error.png");
                }
            }
            FileInputStream fs = null;
            try {
                fs = new FileInputStream(f);
                byte data[] = new byte[1024];
                int len = 0;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while ((len = fs.read(data)) > 0) {
                    out.write(data, 0, len);
                }
                content = out.toByteArray();
            } finally {
                if (fs != null) {
                    fs.close();
                }
            }

        } else {
            FileSystemResource file = new FileSystemResource(this.fullBasePath + path + ext);
            fname = file.getFilename();
            if (ext.equals(".zip")) {
                HashMap<String, byte[]> unzipfiles = FileTools.unZipIt(file.getInputStream());
                if (unzipfiles.size() == 1) {
                    for (String i : unzipfiles.keySet()) {
                        content = unzipfiles.get(i);
                        fname = i;
                        break;
                    }
                } else {
                    content = IOUtils.toByteArray(file.getInputStream());
                }
            }
        }
        response.put("name", fname);
        response.put("content", content);
        return response;
    }
}
