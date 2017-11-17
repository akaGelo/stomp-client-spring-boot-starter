package ru.vyukov.stomp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotation for marking method as receiver.
 * <pre>
 * {@code
 * @Component
 * @Slf4j
 * public class ExampleReceiver {
 *     @literal @Subscribe("/app/hello")
 *      public void hello(Hello helloMessage) {
 *              log.info("Hello " + helloMessage);
 *      }
 * }
 * }
 * </pre>
 *
 * @author Oleg Vyukov
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    /**
     * Subscribe destination
     *
     * @return
     */
    String value();

}
