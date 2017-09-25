package org.ut.util;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileTools {

    public static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void mkDirs(File root, List<String> dirs, int depth) {
        if (depth == 0)
            return;
        for (String s : dirs) {
            File subdir = new File(root, s);
            subdir.mkdir();
            mkDirs(subdir, dirs, depth - 1);
        }
    }

    public static String readFile(String url) {
        String content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            String tmp;
            String s = "";
            while ((tmp = br.readLine()) != null) {
                content += s + tmp;
                s = "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void saveFile(MultipartFile file, String path, boolean rewrite) throws IOException {
        String fileName = file.getOriginalFilename();
        new File(path).mkdirs();
        File f = new File(path + File.separatorChar + fileName + ".zip");
        if (f.exists() && rewrite) {
            f.delete();
        }
        if (f.exists() && !rewrite) {
            throw new FileAlreadyExistsException("Error: " + fileName + " al ready exist");
        }
        ZipOutputStream o = new ZipOutputStream(new FileOutputStream(f));
        ZipEntry e = new ZipEntry(fileName);
        o.putNextEntry(e);
        byte[] data = file.getBytes();
        o.write(data, 0, data.length);
        o.closeEntry();
        o.close();
    }

    public static File[] getFilesFolder(String folderPath) {
        File f = new File(folderPath);
        return f.listFiles();
    }

    public static Map<String, String> getAttributesOfFile(File file) throws IOException {
        Map<String, String> attr = new HashMap<>();
        Path p = Paths.get(file.getAbsolutePath());
        BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
        FileTime fileTime = view.creationTime();
        attr.put("creationTime", new SimpleDateFormat(FileTools.DATETIMEFORMAT).format(fileTime.toMillis()));
        fileTime = view.lastAccessTime();
        attr.put("lastAccessTime", new SimpleDateFormat(FileTools.DATETIMEFORMAT).format(fileTime.toMillis()));
        fileTime = view.lastModifiedTime();
        attr.put("lastModifiedTime", new SimpleDateFormat(FileTools.DATETIMEFORMAT).format(fileTime.toMillis()));
        attr.put("extension", FilenameUtils.getExtension(file.getName()));
        return attr;
    }

    public static boolean isFile(String path) {
        File f = new File(path);
        if (f.isFile())
            return true;
        return false;
    }

}
