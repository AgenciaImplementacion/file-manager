package org.ut.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ut.response.APIVersionsResponse;

@RestController
@PropertySource("classpath:/application.properties")
public class WSController {

	@Value("${org.ut.web.endpoint}")
	private String endpoint;
	
	@RequestMapping(value = { "", "/", "${org.ut.web.endpoint}" })
	public ResponseEntity<?> index() {
		APIVersionsResponse r = new APIVersionsResponse();
		r.addVersion("v1", "/" + endpoint + "/v1");
		return new ResponseEntity<APIVersionsResponse>(r, HttpStatus.OK);
	}

}
