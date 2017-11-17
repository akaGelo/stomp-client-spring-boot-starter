package ru.vyukov.stomp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.vyukov.stomp.example.Hello;

import java.lang.reflect.Type;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Oleg Vyukov
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscribeMethodsInvokerSessionHandlerTest {


    @Mock
    private SubscribeEndpointRegistry registry;

    @Mock
    private StompSession session;

    @Captor
    private ArgumentCaptor<StompFrameHandler> headesCaptor;

    @InjectMocks
    private SubscribeMethodsInvokerSessionHandler underTest;


    @Test
    public void afterConnected() throws Exception {
        when(registry.getAllDestination()).thenReturn(Sets.newSet("/test1", "/test2"));

        underTest.afterConnected(session, null);

        verify(session).subscribe("/test1", underTest);
        verify(session).subscribe("/test2", underTest);
    }

    @Test
    public void getPayloadType() throws Exception {

        when(registry.getMethod("/test")).thenReturn(SubscribeEndpointRegistryTest.createMethod("/test"));

        StompHeaders stompHeaders = stompHeaders("/test");
        Type payloadType = underTest.getPayloadType(stompHeaders);
        assertEquals(Hello.class, payloadType);
    }

    @Test
    public void handleFrame() throws Exception {
        SubscribeMethodInstance mock = mock(SubscribeMethodInstance.class);
        when(registry.getMethod("/test")).thenReturn(mock);

        StompHeaders stompHeaders = stompHeaders("/test");

        Hello payload = new Hello("gelo");
        underTest.handleFrame(stompHeaders, payload);

        verify(mock).invoke(payload);
    }


    @Test
    public void handleFrameNoSubscribeMethod() throws Exception {
        SubscribeMethodInstance mock = mock(SubscribeMethodInstance.class);

        StompHeaders stompHeaders = stompHeaders("/test");

        Hello payload = new Hello("gelo");
        underTest.handleFrame(stompHeaders, payload);

        verify(mock, never()).invoke(any());
    }

    private StompHeaders stompHeaders(String destination) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }

}