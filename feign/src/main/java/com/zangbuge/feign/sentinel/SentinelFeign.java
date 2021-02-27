package com.zangbuge.feign.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.cloud.sentinel.feign.SentinelInvocationHandler;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 源码中的 SentinelFeign
 */
public final class SentinelFeign {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder
            extends Feign.Builder
            implements ApplicationContextAware {
        private Contract contract = new Contract.Default();
        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public Feign build() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {

                @SneakyThrows
                public InvocationHandler create(Target target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
                    Object feignClientFactoryBean = SentinelFeign.Builder.this.applicationContext.getBean("&" + target.type().getName());

                    Class fallback = (Class) SentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "fallback");

                    Class fallbackFactory = (Class) SentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "fallbackFactory");

                    String beanName = (String) SentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "contextId");

                    //获取指定的构造方法
                    Constructor<SentinelInvocationHandler> constructor = SentinelInvocationHandler.class.
                            getDeclaredConstructor(Target.class, Map.class, FallbackFactory.class);
                    constructor.setAccessible(true);

                    if (!StringUtils.hasText(beanName)) {
                        beanName = (String) SentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "name");
                    }
                    if (Void.TYPE != fallback) {
                        Object fallbackInstance = getFromContext(beanName, "fallback", fallback, target
                                .type());
                        return constructor.newInstance(target, dispatch, new FallbackFactory.Default(fallbackInstance));
                    }
                    if (Void.TYPE != fallbackFactory) {
                        FallbackFactory fallbackFactoryInstance = (FallbackFactory) getFromContext(beanName, "fallbackFactory", fallbackFactory, FallbackFactory.class);
                        return constructor.newInstance(target, dispatch, fallbackFactoryInstance);
                    }

                    // 添加默认 FallbackFactory
                    FallbackFactory factory = new FunFallbackFactory(target);
                    return constructor.newInstance(target, dispatch, factory);
                }

                private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
                    Object fallbackInstance = SentinelFeign.Builder.this.feignContext.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(String.format("No %s instance of type %s found for feign client %s", new Object[]{type, fallbackType, name}));
                    }
                    if (!targetType.isAssignableFrom(fallbackType)) {
                        throw new IllegalStateException(String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s", new Object[]{type, fallbackType, targetType, name}));
                    }
                    return fallbackInstance;
                }
            });
            super.contract(new SentinelContractHolder(this.contract));
            return super.build();
        }

        private Object getFieldValue(Object instance, String fieldName) {
            Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
            field.setAccessible(true);
            try {
                return field.get(instance);
            } catch (IllegalAccessException localIllegalAccessException) {
            }
            return null;
        }

        public void setApplicationContext(ApplicationContext applicationContext)
                throws BeansException {
            this.applicationContext = applicationContext;
            this.feignContext = ((FeignContext) this.applicationContext.getBean(FeignContext.class));
        }
    }
}
