package ru.vyukov.stomp;

import org.junit.Test;
import org.springframework.web.socket.WebSocketHttpHeaders;

import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class StompClientPropertiesTest {
    @Test
    public void getHeadersNoAuth() throws Exception {
        StompClientProperties stompClientProperties = new StompClientProperties();
        WebSocketHttpHeaders headers = stompClientProperties.getHeaders();
        assertFalse(headers.containsKey("Authorization"));
        assertEquals(0, headers.size());
    }

    @Test
    public void getHeadersAuth() throws Exception {
        StompClientProperties stompClientProperties = new StompClientProperties();
        StompClientProperties.BasicAuth basicAuth = stompClientProperties.getBasicAuth();

        basicAuth.setPassword("qwerty");
        basicAuth.setUsername("admin");

        WebSocketHttpHeaders headers = stompClientProperties.getHeaders();
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Basic YWRtaW46cXdlcnR5", headers.getFirst("Authorization"));
    }

}