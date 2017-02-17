package com.tts.codelab.oauth2.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "foo-resource", url = "http://codelab:9998")
public interface FooClient {

    @RequestMapping(path = "/foo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String fooRead();
}
