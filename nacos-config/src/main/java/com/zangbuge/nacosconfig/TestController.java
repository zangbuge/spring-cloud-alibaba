package com.zangbuge.nacosconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/10
 */
@Slf4j
@RestController
@RequestMapping
@RefreshScope  // 支持分布式的配置动态刷新
public class TestController {

    /**
     * Data ID: nacos-config (${prefix}-${spring.profiles.active}.${file-extension})
     * GROUP: DEFAULT_GROUP (可以是dev, prod 或项目分组)
     * 配置格式: text
     * 配置内容: nacos.name=lhm
     */
    @Value(value = "${nacos.name}")
    private String name;

    @RequestMapping("/getProp")
    public String getProp() {
        log.info("获取配置: {}", name);
        return name;
    }

    @RequestMapping("/getConfig/{key}")
    public String getConfig(@PathVariable("key") String key) {
        String config = ConfigUtil.getConfig(key);
        log.info("获取配置: {}", config);
        return config;
    }

}
