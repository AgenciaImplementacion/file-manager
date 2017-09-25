package org.ut.web;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ut.driver.DLocalFiles;
import org.ut.driver.DriverNotFoundException;
import org.ut.entity.ServiceInfo;
import org.ut.response.FileServicesResponse;
import org.ut.response.FolderListResponse;
import org.ut.response.MessageResponse;
import org.ut.storage.StorageClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class WSFileController {

    private final static Logger LOGGER = Logger.getLogger(DLocalFiles.class.getName());

    @RequestMapping(value = {"", "/"})
    public ResponseEntity<FileServicesResponse> services() {
        FileServicesResponse r = new FileServicesResponse();
        r.addService("Upload file", "/api/v1/file", ServiceInfo.M_POST);
        r.addService("Update file", "/api/v1/file", ServiceInfo.M_PUT);
        r.addService("Download file", "/api/v1/file/{id}", ServiceInfo.M_GET);
        r.addService("List all files", "/api/v1/file", ServiceInfo.M_GET);
        r.addService("List a segment files", "/api/v1/file/{driver}/{depth}", ServiceInfo.M_GET);
        r.addService("File information", "/api/v1/file/info/{id}", ServiceInfo.M_GET);
        r.addService("Delete File", "/api/v1/file/{id}", ServiceInfo.M_DELETE);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> upload(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("path") String path,
                                                  @RequestParam("driver") String driver) {
        MessageResponse r = new MessageResponse();
        StorageClient st = StorageClient.getInstance();
        try {
            st.store(file, path, driver, false);
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

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> update(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("path") String path,
                                                  @RequestParam("driver") String driver) {
        MessageResponse r = new MessageResponse();
        StorageClient st = StorageClient.getInstance();
        try {
            st.store(file, path, driver, true);
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
    public ResponseEntity<FolderListResponse> listFromADriver(
            @PathVariable("connection") String connection,
            @PathVariable("depth") int depth
    ) {
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
    public ResponseEntity<?> listFromADriverPathOrDownload(
            @PathVariable("connection") String connection,
            @RequestParam("path") String path
    ) {
        if (!path.substring(0, 1).equals("/"))
            path = "/" + path;
        if (path.equals("/"))
            path = "";
        FolderListResponse r = new FolderListResponse();
        StorageClient c = StorageClient.getInstance();
        try {
            if (c.isFile(path, connection)) {
                FileSystemResource file = new FileSystemResource(c.getFullPath(connection) + path);
                byte[] content = IOUtils.toByteArray(file.getInputStream());
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
                return ResponseEntity.ok()
                        .headers(responseHeaders)
                        .contentLength(content.length)
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(content);
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
