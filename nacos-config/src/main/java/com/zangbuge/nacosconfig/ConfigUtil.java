package com.zangbuge.nacosconfig;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/14
 */
@Component
public class ConfigUtil {

    private static ConfigurableEnvironment environment;

    @EventListener(ApplicationStartedEvent.class)
    public void init(ApplicationStartedEvent startedEvent) {
        ConfigurableApplicationContext applicationContext = startedEvent.getApplicationContext();
        environment = applicationContext.getEnvironment();
    }

    public static String getConfig(String key) {
        String property = environment.getProperty(key);
        return property;
    }

}
