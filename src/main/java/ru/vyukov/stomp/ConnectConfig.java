package ru.vyukov.stomp;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;


/**
 * Connection config state for reconnecting
 */
@Value
@NonFinal
class ConnectConfig {

    private long reconnectDelay;

    private String url;

    private WebSocketHttpHeaders handshakeHeaders;

    private StompSessionHandler sessionHandler;


    ConnectConfig(StompClientProperties properties, StompSessionHandler sessionHandler) {
        this.url = properties.getUrl();
        this.sessionHandler = sessionHandler;
        this.handshakeHeaders = properties.getHeaders();
        this.reconnectDelay = properties.getReconnectDelay();
    }
}
