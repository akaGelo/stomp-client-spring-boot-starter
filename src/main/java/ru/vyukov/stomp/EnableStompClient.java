package ru.vyukov.stomp;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable @{@link Subscribe} annotated methods.
 * <p>
 * Configuration .properties
 * <pre>
 * stomp.client.url=ws://127.0.0.1:8080/public/websocket
 *
 * # if necessary
 * #stomp.client.basic-auth.username=agent1
 * #stomp.client.basic-auth.password=cc711bd6-7373-4f89-ba5b-3f4cfd694d36
 *
 *
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(StompClientBootstrapConfiguration.class)
public @interface EnableStompClient {
}
