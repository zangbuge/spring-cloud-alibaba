package com.zangbuge.springcloudalibaba.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/5
 */
@RestController
@RequestMapping
public class TestController {

    @RequestMapping("/test")
    private String test() {
        return "spring cliud alibaba";
    }

}
