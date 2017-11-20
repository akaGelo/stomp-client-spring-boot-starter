package ru.vyukov.stomp.enabletest;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import ru.vyukov.stomp.AbstractConfigurationTest;

/**
 * @author Oleg Vyukov
 */
@TestPropertySource(value = "classpath:test.properties", properties = "stomp.client.enabled=true")
public class EnableForceTest extends AbstractConfigurationTest {
    @Test
    public void testEnabledStompClient() throws Exception {
        assertCount(1);
    }
}
