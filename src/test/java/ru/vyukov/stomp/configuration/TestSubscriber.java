package ru.vyukov.stomp.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.vyukov.stomp.StompClientBootstrapConfigurationTest;
import ru.vyukov.stomp.Subscribe;
import ru.vyukov.stomp.example.Hello;

import javax.annotation.PostConstruct;

/**
 * @author Oleg Vyukov
 */
@Slf4j
public class TestSubscriber {


    private SimpMessagingTemplate simpMessagingTemplate;

    public TestSubscriber(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostConstruct
    public void init() {
        log.info("Test subscriber initialize");
    }


    /**
     * Test send
     *
     * @param hello
     */
    @Subscribe("/junit")
    public void message(Hello hello) {
        log.info("hello received " + hello);
        log.info("sending response ");
        simpMessagingTemplate.convertAndSend("/server/hello", StompClientBootstrapConfigurationTest.HELLO_MESSAGE);
    }


    /**
     * Test send
     *
     * @param hello
     */
    @Subscribe("/junit2")
    public void message2(Hello hello) {
        log.info("hello received " + hello);
        log.info("sending response ");
        simpMessagingTemplate.convertAndSend("/server/hello", StompClientBootstrapConfigurationTest.HELLO_MESSAGE);
    }
}
