package pl.allegro.atl.ports.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpCheckController {

    @GetMapping(path = "/status/health")
    @ResponseStatus(HttpStatus.OK)
    public String healthCheck() {
        return "OK";
    }
}
