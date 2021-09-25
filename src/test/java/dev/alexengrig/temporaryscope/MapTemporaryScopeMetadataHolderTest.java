package dev.alexengrig.temporaryscope;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapTemporaryScopeMetadataHolderTest {

    static final Map<String, TemporaryScopeMetadata> MAP = new HashMap<>();
    static final MapTemporaryScopeMetadataHolder METADATA_HOLDER = new MapTemporaryScopeMetadataHolder(MAP);

    @BeforeEach
    void beforeEach() {
        MAP.clear();
    }

    @Test
    void should_test() {
        String beanName = "testString";
        TemporaryScopeMetadata temporaryScopeMetadata = new TemporaryScopeMetadata(1, ChronoUnit.MINUTES);
        METADATA_HOLDER.put(beanName, temporaryScopeMetadata);
        assertTrue(MAP.containsKey(beanName), "MAP doesn't contain");
        assertEquals(temporaryScopeMetadata, METADATA_HOLDER.get(beanName), "Temporary scope metadata");
    }

}