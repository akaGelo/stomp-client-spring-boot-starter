package ru.vyukov.stomp;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Role;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import static ru.vyukov.stomp.StopmpClientConfigUtils.STOMP_SUBSCRIBE_ANNOTATION_BEAN_POST_PROCESSOR_BEAN_NAME;
import static ru.vyukov.stomp.StopmpClientConfigUtils.STOMP_SUBSCRIBE_ENDPOINT_REGISTRY_BEAN_NAME;

/**
 * Extends for use
 *
 * @author gelo
 */
@Configuration
public class StompClientBootstrapConfiguration {


    @Bean(STOMP_SUBSCRIBE_ANNOTATION_BEAN_POST_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public StompSubscribeAnnotationBeanPostProcessor stompListenerAnnotationBeanPostProcessor() {
        return new StompSubscribeAnnotationBeanPostProcessor();
    }

    @Bean(STOMP_SUBSCRIBE_ENDPOINT_REGISTRY_BEAN_NAME)
    public SubscribeEndpointRegistry subscribeEndpointRegistry() {
        return new SubscribeEndpointRegistry();
    }


    @Bean
    @ConfigurationProperties("controller")
    public WebSocketStompClientProperties wsConfig() {
        return new WebSocketStompClientProperties();
    }

    @Bean
    SimpMessagingTemplate simpMessagingTemplate() {
        SimpMessagingTemplate simpMessagingTemplate = new SimpMessagingTemplate(stompMessageChannel());
        simpMessagingTemplate.setMessageConverter(messageConverter());
        return simpMessagingTemplate;
    }


    @Bean(destroyMethod = "stop")
    @DependsOn(STOMP_SUBSCRIBE_ANNOTATION_BEAN_POST_PROCESSOR_BEAN_NAME)
    StompMessageChannel stompMessageChannel() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        ConnectConfig config = new ConnectConfig(wsConfig(), sessionHandler());
        StompMessageChannel stompMessageChannel = new StompMessageChannel(webSocketClient, taskScheduler(), messageConverter(), config);

        return stompMessageChannel;
    }

    @Bean
    StompSessionHandler sessionHandler() {
        StompSessionHandler sessionHandler = new SubscribeMethodsInvokerSessionHandler(subscribeEndpointRegistry());
        return sessionHandler;
    }

    @Bean
    TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    MessageConverter messageConverter() {
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        return mappingJackson2MessageConverter;
    }


}
