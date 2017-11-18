package ru.vyukov.stomp;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ScheduledFuture;

import static ru.vyukov.stomp.StompMessageChannelTest.testProps;

/**
 * @author Oleg Vyukov
 */
@RunWith(MockitoJUnitRunner.class)
abstract public class SuperStompMessageChannelTest {


    @Mock
    protected WebSocketStompClient webSocketStompClient;

    @Mock
    protected TaskScheduler taskScheduler;


    @Mock
    protected StompSessionHandler stompSessionHandler;

    @Spy
    protected ConnectConfig connectConfig = new ConnectConfig(testProps(), stompSessionHandler);


    @InjectMocks
    protected StompMessageChannel underTest;


}
