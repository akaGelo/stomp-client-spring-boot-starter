package ru.vyukov.stomp;

import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    public void register(String destination, Method method, Class<?> targetClass, Object bean) {
        SubscribeMethodInstance subscribeMethodInstance = new SubscribeMethodInstance(destination, method, bean);
        methodInstances.add(subscribeMethodInstance);
    }
}
