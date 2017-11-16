package ru.vyukov.stomp;

import lombok.Getter;
import lombok.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Link to bean receive method
 *
 * @author gelo
 */
@Getter
public class SubscribeMethodInstance {

    private final String destination;

    private final Method method;

    private final Object beanInstance;
    private Type argType;


    public SubscribeMethodInstance(String destination, Method method, Object beanInstance) {
        this.destination = destination;
        this.method = method;
        this.beanInstance = beanInstance;

        Class<?>[] types = method.getParameterTypes();
        if (1 != types.length) {
            throw new IllegalArgumentException("method " + method + " must have  only one argument");
        }
        this.argType = types[0];
    }

    public void invoke(Object arg) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.invoke(beanInstance, arg);
    }

    public Type getArgType() {
        return argType;
    }
}
