package ru.vyukov.stomp;

import lombok.Value;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;


@Value
class ConnectConfig {

    private long reconnectDelay;

    private String url;

    private WebSocketHttpHeaders handshakeHeaders;

    private StompSessionHandler sessionHandler;


    public ConnectConfig(WebSocketStompClientProperties properties, StompSessionHandler sessionHandler) {
        this.url = properties.getUrl();
        this.sessionHandler = sessionHandler;
        this.handshakeHeaders = properties.getHeaders();
        this.reconnectDelay = properties.getReconnectDelay();
    }
}
