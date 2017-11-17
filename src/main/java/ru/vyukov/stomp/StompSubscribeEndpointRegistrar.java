package ru.vyukov.stomp;

import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Store @Subscribe methods for further registration in @Bean {@link SubscribeEndpointRegistry}
 *
 * @author Oleg Vyukov
 */
class StompSubscribeEndpointRegistrar implements InitializingBean {

    private SubscribeEndpointRegistry registry;

    private List<SubscribeMethodInstance> methodInstances = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        registry.addAll(methodInstances);
    }


    public void setRegistry(SubscribeEndpointRegistry registry) {
        this.registry = registry;
    }

    public void register( Method method, Class<?> targetClass, Object bean) {
        SubscribeMethodInstance subscribeMethodInstance = new SubscribeMethodInstance( method, bean);
        methodInstances.add(subscribeMethodInstance);
    }
}
