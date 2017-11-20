package ru.vyukov.stomp.enabletest;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import ru.vyukov.stomp.AbstractConfigurationTest;

/**
 * @author Oleg Vyukov
 */
@TestPropertySource(value = "classpath:test.properties", properties = "stomp.client.enabled=false")
public class DisableTest extends AbstractConfigurationTest {
    @Test
    public void testDisabledStompClient() throws Exception {

        assertCount(0);
    }
}
