package ru.vyukov.stomp.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.vyukov.stomp.EnableStompClient;
import ru.vyukov.stomp.SubscribeMethodsInvokerSessionHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleg Vyukov
 */
@Configuration
@EnableStompClient
public class EnableStompClientConfig {

    @Bean
    StompSession mockSession() {
        return mock(StompSession.class);
    }

    @Bean
    ListenableFuture<StompSession> mockSessionFuture() throws ExecutionException, InterruptedException, TimeoutException {
        ListenableFuture mock = mock(ListenableFuture.class);
        when(mock.get()).thenReturn(mockSession());
        when(mock.get(anyLong(), any(TimeUnit.class))).thenReturn(mockSession());
        return mock;
    }


    @Bean
    @Primary
    WebSocketStompClient webSocketStompClient() throws InterruptedException, ExecutionException, TimeoutException {
        ListenableFuture<StompSession> stompSessionListenableFuture = mockSessionFuture();

        WebSocketStompClient mock = mock(WebSocketStompClient.class);
        when(mock.connect(anyString(), any(WebSocketHttpHeaders.class), any(SubscribeMethodsInvokerSessionHandler.class)))
                .thenReturn(stompSessionListenableFuture);
        return mock;
    }


    @Bean
    @ConditionalOnBean(SimpMessagingTemplate.class)
    TestSubscriber testSubscriber(SimpMessagingTemplate simpMessagingTemplate) {
        return new TestSubscriber(simpMessagingTemplate);
    }

}
