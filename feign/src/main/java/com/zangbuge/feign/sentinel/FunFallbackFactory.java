package com.zangbuge.feign.sentinel;

import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author: Li Huiming
 * @Date: 2021/2/27
 */
@AllArgsConstructor
public class FunFallbackFactory<T> implements FallbackFactory<T> {

    private final Target<T> target;

    @Override
    public T create(Throwable throwable) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new FunFeignFallBack<>(targetType, targetName, throwable));
        return (T) enhancer.create();
    }

}
