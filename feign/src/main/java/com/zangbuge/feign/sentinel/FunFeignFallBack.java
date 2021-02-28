package com.zangbuge.feign.sentinel;

import com.alibaba.fastjson.JSON;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/27
 */

@Slf4j
@AllArgsConstructor
public class FunFeignFallBack<T> implements MethodInterceptor {

    private final Class<T> targetType;

    private final String serviceName;

    private final Throwable throwable;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        log.error("FunFeignFallBack info targetType: {}, methodName: {}", targetType.getName(), method.getName());
        log.error("FunFeignFallBack info throwable: {}", throwable.getMessage());
        Map<Object, Object> map = new HashMap<>();
        if (!(throwable instanceof FeignException)) {
            log.info("请求的服务不存在", serviceName);
            map.put("code", "500");
            map.put("msg", "请求的服务不存在");
            return JSON.toJSONString(map);
        }
        map.put("code", "501");
        map.put("msg", "FallBack, 请稍后重试");
        return JSON.toJSONString(map);
    }

}
