package ru.vyukov.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

import static org.springframework.util.ReflectionUtils.getAllDeclaredMethods;

/**
 * Find all @{@link Subscribe} methods and add her in {@link StompSubscribeEndpointRegistrar}
 *
 * @author Vyukov Oleg
 */
@Slf4j
public class StompSubscribeAnnotationBeanPostProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private BeanFactory beanFactory;

    private StompSubscribeEndpointRegistrar registrar = new StompSubscribeEndpointRegistrar();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        SubscribeEndpointRegistry registry = beanFactory.getBean(StopmpClientConfigUtils.STOMP_SUBSCRIBE_ENDPOINT_REGISTRY_BEAN_NAME, SubscribeEndpointRegistry.class);
        registrar.setRegistry(registry);
        registrar.afterPropertiesSet();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);

        for (Method method : getAllDeclaredMethods(targetClass)) {
            Subscribe ann = method.getAnnotation(Subscribe.class);
            if (null != ann) {
                registerSubscribeMethod(ann.value(), method, targetClass, bean);
            }
        }
        return bean;

    }

    private void registerSubscribeMethod(String mapping, Method method, Class<?> targetClass, Object bean) {
        log.info("Subscribe " + mapping + " " + method);
        registrar.register(method, targetClass, bean);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}