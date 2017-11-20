package ru.vyukov.stomp;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.vyukov.stomp.configuration.EnableStompClientConfig;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

/**
 * @author Oleg Vyukov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EnableStompClientConfig.class, ValidationAutoConfiguration.class}, initializers = ConfigFileApplicationContextInitializer.class)
@DirtiesContext(methodMode = BEFORE_METHOD)
@Configuration
@EnableConfigurationProperties
abstract  public class AbstractConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;


    public void assertCount(int expected) {
        assertEquals(expected, applicationContext.getBeanNamesForType(StompMessageChannel.class).length);
        assertEquals(expected, applicationContext.getBeanNamesForType(SimpMessagingTemplate.class).length);
    }
}
