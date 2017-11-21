package ru.vyukov.stomp.events;

import lombok.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * @author Oleg Vyukov
 */
@Value
public class StompTransportErrorEvent {

    private StompSession session;

    private Throwable exception;

}
