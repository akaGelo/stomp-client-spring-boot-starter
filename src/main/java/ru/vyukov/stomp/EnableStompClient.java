package ru.vyukov.stomp;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(StompClientBootstrapConfiguration.class)
public @interface EnableStompClient {
}
