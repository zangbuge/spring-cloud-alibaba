package com.zangbuge.feign.sentinel;

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

    private final String targetName;

    private final Throwable throwable;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        log.error("FunFeignFallBack: targetType: {}, targetName: {}, throwable: {}", targetType.getName(), targetName, throwable.getMessage());
        Map<Object, Object> map = new HashMap<>();
        if (!(throwable instanceof FeignException)) {
            map.put("code", "500");
            map.put("msg", "系统繁忙,请稍后重试");
            return map;
        }
        FeignException exception = (FeignException) throwable;
        log.error("Feign请求异常", exception);
        map.put("code", "501");
        map.put("msg", "请求失败,请稍后重试");
        return map;
    }

}
