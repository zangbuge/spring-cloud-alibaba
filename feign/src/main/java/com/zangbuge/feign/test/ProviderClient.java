package com.zangbuge.feign.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/** 熔断 @FeignClient 指定 fallback = FallbackService.class  FallbackService 实现ProviderClient接口
 * @Author: Li Huiming
 * @Date: 2021/2/6
 */
@FeignClient("nacos-service")
public interface ProviderClient {

    @RequestMapping("/test")
    String test();

}
