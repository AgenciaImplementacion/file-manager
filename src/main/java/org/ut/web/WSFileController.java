package org.ut.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ut.driver.DLocalFiles;
import org.ut.entity.ServiceInfo;
import org.ut.response.FileServicesResponse;
import org.ut.response.MessageResponse;
import org.ut.storage.StorageClient;

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
        r.addService("List a segment files", "/api/v1/file?offset={#}&limit={#}", ServiceInfo.M_GET);
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
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse> list() {
        MessageResponse r = new MessageResponse();
        StorageClient c = StorageClient.getInstance();
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

}
