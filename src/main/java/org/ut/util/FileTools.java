package org.ut.util;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.*;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;

public class FileTools {

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

    public static void saveFile(MultipartFile file, String path) throws IOException {
            String realFileName;
            String fileName = file.getName();
            if (fileName.lastIndexOf(File.separator) >= 0) {
                realFileName = path + File.separatorChar
                        + fileName.substring(fileName.lastIndexOf(File.separatorChar));
            } else {
                realFileName = path + File.separatorChar
                        + fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1);
            }
            File f = new File(path + File.separatorChar + file.getName() + ".zip");
            ZipOutputStream o = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry e = new ZipEntry(realFileName);
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

}
