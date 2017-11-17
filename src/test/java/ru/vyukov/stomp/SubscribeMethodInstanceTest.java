package ru.vyukov.stomp;

import org.junit.Test;
import ru.vyukov.stomp.configuration.TestSubscriber;
import ru.vyukov.stomp.example.Hello;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class SubscribeMethodInstanceTest {
    @Test
    public void getArgType() throws Exception {
        TestSubscriber testSubscriber = new TestSubscriber(null);
        Method method = testSubscriber.getClass().getMethod("message", Hello.class);

        SubscribeMethodInstance subscribeMethodInstance = new SubscribeMethodInstance(method, testSubscriber);

        Type argType = subscribeMethodInstance.getArgType();
        assertEquals(Hello.class, argType);

        String destination = subscribeMethodInstance.getDestination();
        assertEquals("/junit", destination);
    }

}