package ru.vyukov.stomp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class StompMessageChannelNotStartedTest extends SuperStompMessageChannelTest {


    @Test(expected = IllegalStateException.class)
    public void start() throws Exception {
        underTest.send(null);
    }

}