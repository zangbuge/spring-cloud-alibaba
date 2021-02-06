package com.zangbuge.nacosservice.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/5
 */
@RestController
@RequestMapping
public class TestController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/test")
    private String test() {
        return "spring cliud alibaba from: " + port;
    }

}
