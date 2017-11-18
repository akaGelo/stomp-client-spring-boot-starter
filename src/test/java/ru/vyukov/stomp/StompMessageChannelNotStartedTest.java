package ru.vyukov.stomp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class StompMessageChannelNotStartedTest extends SuperStompMessageChannelTest {


    @Test()
    public void start() throws Exception {
        try {
            underTest.send(null);
            fail("expected IllegalStateException");
        } catch (Exception e) {
            if (!(e.getCause() instanceof IllegalStateException)) {
                fail("expected IllegalStateException");
            }
        }
    }

}