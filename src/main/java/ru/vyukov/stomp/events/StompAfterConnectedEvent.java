package ru.vyukov.stomp.events;

import lombok.Value;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * @author Oleg Vyukov
 */
@Value
public class StompAfterConnectedEvent {

    private StompSession session;

    private StompHeaders connectedHeaders;

}
