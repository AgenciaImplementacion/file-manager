package org.ut.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ut.driver.DLocalFiles;
import org.ut.driver.DriverNotFoundException;
import org.ut.entity.ServiceInfo;
import org.ut.response.FileServicesResponse;
import org.ut.response.FolderListResponse;
import org.ut.response.MessageResponse;
import org.ut.storage.StorageClient;
import org.ut.util.RandomString;

@RestController
@PropertySource("classpath:/application.properties")
@RequestMapping("/${org.ut.web.endpoint}/v1")
public class WSFileController {

    @Value("${org.ut.web.endpoint}")
    private String endpoint;

    private final static Logger LOGGER = Logger.getLogger(DLocalFiles.class.getName());

    @RequestMapping(value = { "", "/" })
    public ResponseEntity<FileServicesResponse> services() {
        FileServicesResponse r = new FileServicesResponse();
        r.addService("Upload file", "/" + endpoint + "/v1/file", ServiceInfo.M_POST);
        r.addService("Update file", "/" + endpoint + "/v1/file", ServiceInfo.M_PUT);
        r.addService("Download file", "/" + endpoint + "/v1/file/{id}", ServiceInfo.M_GET);
        r.addService("List all files", "/" + endpoint + "/v1/file", ServiceInfo.M_GET);
        r.addService("List a segment files", "/" + endpoint + "/v1/file/{driver}/{depth}", ServiceInfo.M_GET);
        r.addService("File information", "/" + endpoint + "/v1/file/info/{id}", ServiceInfo.M_GET);
        r.addService("Delete File", "/" + endpoint + "/v1/file/{id}", ServiceInfo.M_DELETE);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> upload(@RequestParam("file") MultipartFile file,
            @RequestParam("driver") String driver, @RequestParam("namespace") Optional<String> opNamespace) {
        MessageResponse r = new MessageResponse();
        StorageClient st = StorageClient.getInstance();
        String namespace = "default";
        if (opNamespace.isPresent()) {
            namespace = opNamespace.get();
        }
        try {
            int y = Calendar.getInstance().get(Calendar.YEAR);
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int mi = Calendar.getInstance().get(Calendar.MINUTE);
            String s = (new RandomString(5)).nextString();
            String base_url = namespace + File.separatorChar + String.valueOf(y) + File.separatorChar
                    + String.valueOf(m) + File.separatorChar + String.valueOf(d);
            while (true) {
                try {
                    st.store(file, h + "h" + mi + "m" + s, base_url, driver, false);
                    break;
                } catch (FileAlreadyExistsException e) {
                    s = (new RandomString(5)).nextString();
                }
            }
            r.setUrl("/v1/file/" + driver + "?id=" + base_url.replaceAll(String.valueOf(File.separatorChar), ".") + "."
                    + h + "h" + mi + "m" + s);
            r.setOk("Success");
        } catch (IOException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.store.IOException) " + e.getMessage(), e);
        } catch (DriverNotFoundException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.store.DriverNotFoundException) " + e.getMessage(), e);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> update(@RequestParam("file") MultipartFile file,
            @RequestParam("id") String id, @RequestParam("driver") String driver) {

        MessageResponse r = new MessageResponse();
        StorageClient st = StorageClient.getInstance();
        String[] s = id.split(".");
        String name = s[s.length - 1];
        String path = id.replaceAll(".", String.valueOf(File.separatorChar)).replaceAll(File.separatorChar + name, "");
        try {
            st.store(file, name, path, driver, true);
            r.setOk("Success");
        } catch (FileAlreadyExistsException e) {
            r.setError("File already exists");
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.store.FileNotFoundException) " + e.getMessage(), e);
        } catch (IOException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.store.IOException) " + e.getMessage(), e);
        } catch (DriverNotFoundException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.store.DriverNotFoundException) " + e.getMessage(), e);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<FolderListResponse> list() {
        FolderListResponse r = new FolderListResponse();
        StorageClient c = StorageClient.getInstance();
        try {
            for (String driver : c.getDriverList()) {
                r.addData(c.list("", driver));
            }
            r.setOk("Success");
        } catch (DriverNotFoundException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.DriverNotFoundException) " + e.getMessage(), e);
        } catch (IOException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.IOException) " + e.getMessage(), e);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file/{connection}/{depth}", method = RequestMethod.GET)
    public ResponseEntity<FolderListResponse> listFromADriver(@PathVariable("connection") String connection,
            @PathVariable("depth") int depth) {
        FolderListResponse r = new FolderListResponse();
        StorageClient c = StorageClient.getInstance();
        try {
            r.addData(c.list("", connection, depth));
            r.setOk("Success");
        } catch (DriverNotFoundException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.DriverNotFoundException) " + e.getMessage(), e);
        } catch (IOException e) {
            r.setError(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.IOException) " + e.getMessage(), e);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file/{connection}", method = RequestMethod.GET)
    public ResponseEntity<?> listFromADriverPathOrDownload(@PathVariable("connection") String connection,
            @RequestParam("id") String id, @RequestParam("thumbnail") Optional<Boolean> thumbnail,
            @RequestParam("size") Optional<String> size) {

        String path = id.replaceAll("\\.", String.valueOf(File.separatorChar));
        if (!path.substring(0, 1).equals("/")) {
            path = "/" + path;
        }
        if (path.equals("/")) {
            path = "";
        }

        String thbsize="60";
        if(size.isPresent()){
        switch(size.get()){
            case "medium":
            thbsize="96";
            break;
            case "large":
            thbsize="120";
            break;
        }
    }

        FolderListResponse r = new FolderListResponse();
        StorageClient c = StorageClient.getInstance();
        if (thumbnail.isPresent()) {
            try {
                Map<String, Object> f = c.getFile(path, connection, (thumbnail.isPresent() ? thumbnail.get() : false), thbsize);
                byte[] content = (byte[]) f.get("content");
                return ResponseEntity.ok().contentLength(content.length)
                        .contentType(MediaType.parseMediaType("image/png")).body(content);
            } catch (DriverNotFoundException e) {
                r.setError(e.getMessage());
                LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.DriverNotFoun dException) " + e.getMessage(), e);
            } catch (IOException e) {
                r.setError(e.getMessage());
                LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.IOException) " + e.getMessage(), e);
            }
            return new ResponseEntity<>(r, HttpStatus.OK);
        } else {
            try {
                if (c.isFile(path, connection)) {
                    Map<String, Object> f = c.getFile(path, connection,
                            (thumbnail.isPresent() ? thumbnail.get() : false), thbsize);
                    byte[] content = (byte[]) f.get("content");
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.add("Content-Disposition", "attachment; filename=\"" + f.get("name") + "\"");
                    return ResponseEntity.ok().headers(responseHeaders).contentLength(content.length)
                            .contentType(MediaType.parseMediaType("application/octet-stream")).body(content);
                } else {
                    r.addData(c.list(path, connection, 1));
                    r.setOk("Success");
                }
            } catch (DriverNotFoundException e) {
                r.setError(e.getMessage());
                LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.DriverNotFoundException) " + e.getMessage(), e);
            } catch (IOException e) {
                r.setError(e.getMessage());
                LOGGER.log(Level.SEVERE, "Error: (DLocalFiles.list.IOException) " + e.getMessage(), e);
            }
            return new ResponseEntity<>(r, HttpStatus.OK);
        }
    }

}
