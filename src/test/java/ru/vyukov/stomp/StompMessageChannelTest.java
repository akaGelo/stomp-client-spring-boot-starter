package ru.vyukov.stomp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author Oleg Vyukov
 */
public class StompMessageChannelTest extends SuperStompMessageChannelTest {


    private ListenableFuture<StompSession> stompSessionFuture;

    private ScheduledFuture reconnectFuture;


    @Before
    public void setUp() throws Exception {
        stompSessionFuture = mock(ListenableFuture.class);
        reconnectFuture = mock(ScheduledFuture.class);

        when(webSocketStompClient.connect(anyString(), any(WebSocketHttpHeaders.class), any(StompSessionHandler.class))).thenReturn(stompSessionFuture);
        when(taskScheduler.scheduleWithFixedDelay(any(), anyLong())).thenReturn(reconnectFuture);

        underTest.start();
    }


    @Test
    public void testConnected() {
        verify(webSocketStompClient).connect(anyString(), any(WebSocketHttpHeaders.class), any(StompSessionHandler.class));
    }

    @Test
    public void createReconnectTask() throws Exception {
        verify(taskScheduler).scheduleWithFixedDelay(any(Runnable.class), eq(testProps().getReconnectDelay()));
    }

    @Test
    public void send() throws Exception {
        final String destination = "/test";

        StompSession session = mock(StompSession.class);
        Message<byte[]> message = createTestMessage(destination);
        when(stompSessionFuture.get(5_000, MILLISECONDS)).then(inv -> {
            Thread.sleep(4_000);
            return session;
        });

        Instant start = Instant.now();
        assertTrue(underTest.send(message, 5_000));

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        assertTrue(duration.getSeconds() >= 3);

        verify(session).send(destination, message.getPayload());
    }

    @Test()
    public void sendTimeout() throws Exception {
        final String destination = "/test";

        StompSession session = mock(StompSession.class);
        Message<byte[]> message = createTestMessage(destination);
        when(stompSessionFuture.get(1_000, MILLISECONDS)).then(inv -> {
            Thread.sleep(1_000);
            throw new TimeoutException();
        });

        boolean test = false;
        try {
            underTest.send(message, 1_000);
            fail("no exception");
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TimeoutException) {
                test = true;
            }
        }
        assertTrue(test);

        verify(session, never()).send(anyString(), any());
        verify(stompSessionFuture).cancel(true);
    }

    private Message<byte[]> createTestMessage(String destination) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("simpDestination", destination);
        GenericMessage<byte[]> genericMessage = new GenericMessage<byte[]>(new byte[0], headers);
        return genericMessage;
    }

    @Test(expected = IllegalStateException.class)
    public void startException() throws Exception {
        underTest.start();
    }

    @Test
    public void stop() throws Exception {
        underTest.stop();

        verify(reconnectFuture).cancel(true);
        verify(webSocketStompClient).stop();
    }


    static StompClientProperties testProps() {
        StompClientProperties stompClientProperties = new StompClientProperties();
        stompClientProperties.setUrl("http://example.com");
        stompClientProperties.setReconnectDelay(5_000);
        return stompClientProperties;
    }
}