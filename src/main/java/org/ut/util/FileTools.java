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
import java.util.zip.*;
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

    public static void saveFile(MultipartFile file, String name, String path, boolean rewrite) throws IOException {
        // String fileName = file.getOriginalFilename();
        new File(path).mkdirs();
        File f = new File(path + File.separatorChar + name + ".zip");
        if (f.exists() && rewrite) {
            f.delete();
        }
        if (f.exists() && !rewrite) {
            throw new FileAlreadyExistsException("Error: " + name + " al ready exist");
        }
        ZipOutputStream o = new ZipOutputStream(new FileOutputStream(f));
        ZipEntry e = new ZipEntry(file.getOriginalFilename());
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

    /**
     * Unzip it
     * 
     * @param zipFile InputStream zip file
     */
    public static HashMap<String, byte[]> unZipIt(InputStream zipFile) {
        HashMap<String, byte[]> files = new HashMap<>();
        try {
            ZipInputStream zis = new ZipInputStream(zipFile);
            ZipEntry ze = zis.getNextEntry();
            if (zis != null) {
                while (ze != null) {
                    String fileName = ze.getName();
                    int bytesToRead = zis.available();
                    if (bytesToRead != -1) {
                        byte data[] = new byte[1024];
                        int len = 0;
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        while ((len = zis.read(data)) > 0) {
                            out.write(data, 0, len);
                        }
                        files.put(fileName, out.toByteArray());
                    }
                    ze = zis.getNextEntry();
                }
                zis.closeEntry();
                zis.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return files;
    }

    public static void generateThumbnail(MultipartFile input, String path) throws IOException {

        String format = ".png";

        String ext = FilenameUtils.getExtension(input.getOriginalFilename());

        String[] images = { "png", "jpg", "jpeg", "gif", "bmp", "tif", "pdf" };
        if (Arrays.stream(images).anyMatch(ext::equals)) {
            File f = new File("/tmp/" + input.getOriginalFilename());
            input.transferTo(f);
            ProcessBuilder processBuilder = new ProcessBuilder();
            String command = "convert -resize x120 /tmp/" + input.getOriginalFilename() + "[0] /tmp/"
                    + input.getOriginalFilename() + "120" + format+ " & convert -resize x96 /tmp/" + input.getOriginalFilename() + "[0] /tmp/"
                    + input.getOriginalFilename() + "96" + format+" & convert -resize x60 /tmp/" + input.getOriginalFilename() + "[0] /tmp/"
                    + input.getOriginalFilename() + "60" + format;
            processBuilder.command("bash", "-c", command);
            try {
                Process process = processBuilder.start();
                StringBuilder output = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    File t = new File("/tmp/" + input.getOriginalFilename() + "120" + format);
                    t.renameTo(new File(path + "120" + format));
                    t = new File("/tmp/" + input.getOriginalFilename() + "96" + format);
                    t.renameTo(new File(path + "96" + format));
                    t = new File("/tmp/" + input.getOriginalFilename() + "60" + format);
                    t.renameTo(new File(path + "60" + format));
                } else {
                    System.out.println("Error: " + output.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            f.delete();
        }
    }

}
