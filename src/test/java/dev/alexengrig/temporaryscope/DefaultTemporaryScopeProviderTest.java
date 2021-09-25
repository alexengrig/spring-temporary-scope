package dev.alexengrig.temporaryscope;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class DefaultTemporaryScopeProviderTest {

    static final Map<String, TemporaryScopeBean> BEAN_MAP = new HashMap<>();
    static final Map<String, TemporaryScopeMetadata> METADATA_MAP = new HashMap<>();
    static final MapTemporaryScopeBeanHolder BEAN_HOLDER = new MapTemporaryScopeBeanHolder(BEAN_MAP);
    static final MapTemporaryScopeMetadataHolder METADATA_HOLDER = new MapTemporaryScopeMetadataHolder(METADATA_MAP);
    static final DefaultTemporaryScopeProvider PROVIDER = new DefaultTemporaryScopeProvider(BEAN_HOLDER, METADATA_HOLDER);
    static final String BEAN_NAME = "testBean";
    static final int AMOUNT = 1;
    static final ChronoUnit UNIT = ChronoUnit.SECONDS;
    static final long MILLIS = UNIT.getDuration().multipliedBy(AMOUNT).toMillis();
    static final Supplier<Object> BEAN_PRODUCER = LocalDateTime::now;

    @BeforeEach
    void beforeEach() {
        BEAN_MAP.clear();
        METADATA_MAP.clear();
        METADATA_HOLDER.put(BEAN_NAME, new TemporaryScopeMetadata(AMOUNT, UNIT));
    }

    @Test
    void should_get() throws InterruptedException {
        Object firstGottenBean = PROVIDER.get(BEAN_NAME, BEAN_PRODUCER);
        Thread.sleep(MILLIS);
        Object secondGottenBean = PROVIDER.get(BEAN_NAME, BEAN_PRODUCER);
        assertNotEquals(firstGottenBean, secondGottenBean, "The second call should create a new bean");
        Object thirdGottenBean = PROVIDER.get(BEAN_NAME, BEAN_PRODUCER);
        assertSame(secondGottenBean, thirdGottenBean, "The third call should return the second bean");
    }

    @Test
    void should_throw_onGet_withoutMetadata() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PROVIDER.get("nonexistentBean", () -> fail("Should no call")));
        assertEquals("No bean metadata for bean name: nonexistentBean", exception.getMessage(), "Message");
    }

    @Test
    void should_remove() {
        Object nothing = PROVIDER.remove(BEAN_NAME);
        assertNull(nothing, "Should be no a bean");
        Object gottenBean = PROVIDER.get(BEAN_NAME, BEAN_PRODUCER);
        assertNotNull(gottenBean, "Should be a bean");
        Object removedBean = PROVIDER.remove(BEAN_NAME);
        assertSame(gottenBean, removedBean, "Gotten and removed beans");
    }

}