package ru.vyukov.stomp.enabletest;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import ru.vyukov.stomp.AbstractConfigurationTest;

/**
 * @author Oleg Vyukov
 */
@TestPropertySource(value = "classpath:test.properties")
public class EnableDefaultTest extends AbstractConfigurationTest {

    @Test
    public void testEnabledStompClient() throws Exception {
        assertCount(1);
    }
}
