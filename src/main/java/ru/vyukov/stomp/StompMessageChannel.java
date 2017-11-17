package ru.vyukov.stomp;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.DESTINATION_HEADER;

/**
 * Message channel to stomp server. Automatic reconnect
 */
@Slf4j
class StompMessageChannel implements MessageChannel, SmartInitializingSingleton {


    private final WebSocketStompClient webSocketStompClient;

    private final TaskScheduler taskScheduler;

    private volatile ListenableFuture<StompSession> sessionFuture;

    private final ConnectConfig connectConfig;

    private volatile Future<?> reconnectTaskFuture;


    public StompMessageChannel(WebSocketStompClient webSocketStompClient, ConnectConfig connectConfig, TaskScheduler taskScheduler) {
        this.webSocketStompClient = webSocketStompClient;
        this.connectConfig = connectConfig;
        this.taskScheduler = taskScheduler;
    }


    public void start() {
        if (null != reconnectTaskFuture) {
            throw new IllegalStateException("Already started");
        }
        connect();
        reconnectTaskFuture = taskScheduler.scheduleWithFixedDelay(createReconnectTask(), connectConfig.getReconnectDelay());
    }

    public void stop() {
        if (null == connectConfig) {
            throw new IllegalStateException("Not started");
        }
        reconnectTaskFuture.cancel(true);
        webSocketStompClient.stop();
    }


    protected void connect() {
        if (null != sessionFuture) {
            sessionFuture.cancel(true);
        }
        WebSocketHttpHeaders handshakeHeaders = connectConfig.getHandshakeHeaders();

        sessionFuture = webSocketStompClient.connect(connectConfig.getUrl(), handshakeHeaders, connectConfig.getSessionHandler());
    }


    @Override
    public boolean send(Message<?> message) {
        return send(message, MessageChannel.INDEFINITE_TIMEOUT);
    }

    @Override
    public boolean send(Message<?> message, long timeout) {
        try {
            StompSession stompSession = getSessionWithTimeout(timeout);
            String destination = (String) message.getHeaders().get(DESTINATION_HEADER);
            Object payload = message.getPayload();
            stompSession.send(destination, payload);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ListenableFuture<StompSession> getSessionFuture() {
        if (null == sessionFuture) {
            throw new IllegalStateException("session not started");
        }
        return sessionFuture;
    }

    private StompSession getSessionWithDefaultTimeout() throws Exception {
        return getSessionWithTimeout(connectConfig.getReconnectDelay());
    }

    synchronized private StompSession getSessionWithTimeout(long timeout) throws Exception {
        try {
            return getSessionFuture().get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            getSessionFuture().cancel(true);
            throw e;
        }
    }


    protected Runnable createReconnectTask() {
        return () -> {
            try {
                StompSession stompSession = getSessionWithDefaultTimeout();
                if (!stompSession.isConnected()) {
                    throw new Exception("Session not connected");
                }
            } catch (InterruptedException e) {
                log.info("Interrupted", e);
            } catch (Exception e) {
                log.info("Session problem", e);
                log.info("Renew session");
                connect();
            }
        };
    }

    @Override
    public void afterSingletonsInstantiated() {
        start();
    }
}
