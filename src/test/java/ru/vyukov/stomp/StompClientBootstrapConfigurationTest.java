package ru.vyukov.stomp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.vyukov.stomp.StompMessageChannel;
import ru.vyukov.stomp.configuration.EnableStompClientConfig;
import ru.vyukov.stomp.example.Hello;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

/**
 * @author Oleg Vyukov
 */

@TestPropertySource("classpath:test.properties")
public class StompClientBootstrapConfigurationTest extends AbstractConfigurationTest {

    public static final Hello HELLO_MESSAGE = new Hello("gelo");
    private static final byte[] SERIALIZED_HELLO_MESSAGE = new byte[]{123, 34, 110, 97, 109, 101, 34, 58, 34, 103, 101, 108, 111, 34, 125};


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    private StompMessageChannel stompMessageChannel;

    @Autowired
    private StompSession stompSession;

    @Autowired
    private StompSessionHandler stompSessionHandler;


    @Test
    public void testConvertAndSend() throws Exception {
        simpMessagingTemplate.convertAndSend("/junit", HELLO_MESSAGE);
        verify(stompSession).send(eq("/junit"), eq(SERIALIZED_HELLO_MESSAGE));
    }

    @Test
    public void testSubscribeAnnotations() throws Exception {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setDestination("/junit");

        stompSessionHandler.handleFrame(stompHeaders, new Hello("STOMP"));

        verify(stompSession).send(eq("/server/hello"), eq(SERIALIZED_HELLO_MESSAGE));
    }
}