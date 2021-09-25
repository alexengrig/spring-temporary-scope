package dev.alexengrig.temporaryscope;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapTemporaryScopeBeanHolderTest {

    static final Map<String, TemporaryScopeBean> MAP = new HashMap<>();
    static final MapTemporaryScopeBeanHolder BEAN_HOLDER = new MapTemporaryScopeBeanHolder(MAP);

    @BeforeEach
    void beforeEach() {
        MAP.clear();
    }

    @Test
    void should_test() {
        String beanName = "testString";
        TemporaryScopeBean temporaryScopeBean = new TemporaryScopeBean(LocalDateTime.now(), "Test string");
        BEAN_HOLDER.put(beanName, temporaryScopeBean);
        assertTrue(MAP.containsKey(beanName), "MAP doesn't contain");
        assertTrue(BEAN_HOLDER.contains(beanName), "BEAN_HOLDER doesn't contain");
        assertEquals(temporaryScopeBean, BEAN_HOLDER.get(beanName), "Gotten temporary scope bean");
        assertEquals(temporaryScopeBean, BEAN_HOLDER.remove(beanName), "Removed temporary scope bean");
        assertFalse(MAP.containsKey(beanName), "MAP contains");
    }

}