package com.zangbuge.feign.test;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/** 熔断 @FeignClient 指定 fallback = FallbackService.class  FallbackService 实现ProviderClient接口
 * @Author: Li Huiming
 * @Date: 2021/2/6
 */
@Headers({
        "Content-Type: " + MediaType.APPLICATION_JSON_UTF8_VALUE
})
@ResponseBody
@FeignClient("nacos-service")
public interface ProviderClient {

    @RequestMapping("/test")
    String test();

}
