package ru.vyukov.stomp;

import org.junit.Before;
import org.junit.Test;
import ru.vyukov.stomp.configuration.TestSubscriber;
import ru.vyukov.stomp.example.Hello;

import java.lang.reflect.Method;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class SubscribeEndpointRegistryTest {

    private SubscribeEndpointRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new SubscribeEndpointRegistry();
    }

    @Test
    public void add() throws Exception {
        registry.addAll(asList(createMethod("/test"), createMethod("/test2"), createMethod("/tes3")));
        int size = registry.getAllDestination().size();
        assertEquals(3, size);
    }

    @Test(expected = IllegalStateException.class)
    public void addDuplicate() throws Exception {
        registry.addAll(asList(createMethod("/test"), createMethod("/test"), createMethod("/tes3")));
    }


    @Test
    public void getMethod() throws Exception {
        registry.addAll(asList(createMethod("/test"), createMethod("/test2"), createMethod("/tes3")));
        SubscribeMethodInstance method = registry.getMethod("/test2");
        assertEquals("/test2", method.getDestination());
    }


    public static SubscribeMethodInstance createMethod(final String destination) throws NoSuchMethodException {
        Object testSubscriber = new Object() {
            @Subscribe("/change/")
            public void message(Hello hello) {
            }
        };
        Method method = testSubscriber.getClass().getMethod("message", Hello.class);
        return new SubscribeMethodInstance(method, testSubscriber) {
            @Override
            public String getDestination() {
                return destination;
            }
        };
    }

}