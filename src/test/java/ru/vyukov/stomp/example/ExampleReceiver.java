package ru.vyukov.stomp.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.vyukov.stomp.Subscribe;

/**
 * @author Oleg Vyukov
 */
@Component
@Slf4j
public class ExampleReceiver {

    @Subscribe("/app/hello")
    public void hello(Hello helloMessage) {
        log.info("Hello " + helloMessage);
    }
}
