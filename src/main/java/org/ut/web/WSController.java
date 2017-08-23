package org.ut.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.ut.response.APIVersionsResponse;

@RestController
public class WSController {

    @RequestMapping(value={"", "/", "api"})
    public ResponseEntity<?> index() {
        APIVersionsResponse r = new APIVersionsResponse();
        r.addVersion("v1","/api/v1");
        return new ResponseEntity<APIVersionsResponse>(r, HttpStatus.OK);
    }




}
