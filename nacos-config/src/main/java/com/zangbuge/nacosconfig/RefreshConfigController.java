package com.zangbuge.nacosconfig;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/6
 */
@RefreshScope
@RestController
@RequestMapping("/refresh_config")
public class RefreshConfigController {

}
