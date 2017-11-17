package ru.vyukov.stomp;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Link to bean receive method
 *
 * @author gelo
 */
@Getter
class SubscribeMethodInstance {

    private final String destination;

    private final Method method;

    private final Object beanInstance;

    private final Type argType;


    public SubscribeMethodInstance(Method method, Object beanInstance) {
        this.destination = extractDestination(method);
        this.method = method;
        this.beanInstance = beanInstance;
        this.argType = extractArgType(method);
    }


    private String extractDestination(Method method) {
        return method.getAnnotation(Subscribe.class).value();
    }

    public void invoke(Object arg) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.invoke(beanInstance, arg);
    }

    public Type getArgType() {
        return argType;
    }

    private Type extractArgType(Method method) {
        Class<?>[] types = method.getParameterTypes();
        if (1 != types.length) {
            throw new IllegalArgumentException("method " + method + " must have  only one argument");
        }
        return types[0];
    }
}
