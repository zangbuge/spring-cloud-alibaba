package com.zangbuge.feign.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/6
 */

@Slf4j
@RestController
@RequestMapping
public class FeignController {

    @Autowired
    private ProviderClient providerClient;

    @RequestMapping("/testFeign")
    public String testFeign() {
        log.info("testFeign");
        String test = providerClient.test();
        return test;
    }

}
