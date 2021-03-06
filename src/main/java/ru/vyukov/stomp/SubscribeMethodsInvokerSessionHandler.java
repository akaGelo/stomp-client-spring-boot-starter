package ru.vyukov.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import ru.vyukov.stomp.events.StompAfterConnectedEvent;
import ru.vyukov.stomp.events.StompTransportErrorEvent;

import java.lang.reflect.Type;

/**
 * This session handler invoke @Subscribe methods by destination on receive messages
 *
 * @author Oleg Vyukov
 */
@Slf4j
public class SubscribeMethodsInvokerSessionHandler extends StompSessionHandlerAdapter {


    private final SubscribeEndpointRegistry endpointRegistry;

    private ApplicationEventPublisher eventPublisher;

    public SubscribeMethodsInvokerSessionHandler(SubscribeEndpointRegistry endpointRegistry, ApplicationEventPublisher eventPublisher) {
        this.endpointRegistry = endpointRegistry;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session: {}", session.getSessionId());
        eventPublisher.publishEvent(new StompAfterConnectedEvent(session, connectedHeaders));

        endpointRegistry.getAllDestination().forEach((destination) -> subscribe(destination, session));
        if (endpointRegistry.getAllDestination().isEmpty()) {
            log.warn("No @Subscribe methods");
        }
    }


    private void subscribe(String destination, StompSession session) {
        session.subscribe(destination, this);
        log.debug("Subscribe " + destination + " from session " + session);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        log.error("Stomp error", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Stomp transport error", exception);
        eventPublisher.publishEvent(new StompTransportErrorEvent(session, exception));
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        String destination = headers.getDestination();
        SubscribeMethodInstance first = endpointRegistry.getMethod(destination);
        return first.getArgType();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String destination = headers.getDestination();
        SubscribeMethodInstance subscribeMethodInstance = endpointRegistry.getMethod(destination);
        try {
            subscribeMethodInstance.invoke(payload);
        } catch (Exception e) {
            log.error("Invoke subscribe destination=[" + destination + "] method exception. See @Subscribe", e);
        }
    }
}