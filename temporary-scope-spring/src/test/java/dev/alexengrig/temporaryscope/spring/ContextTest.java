package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.TemporaryScopeBeanHolder;
import dev.alexengrig.temporaryscope.TemporaryScopeMetadata;
import dev.alexengrig.temporaryscope.TemporaryScopeMetadataHolder;
import dev.alexengrig.temporaryscope.TemporaryScopeProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static dev.alexengrig.temporaryscope.spring.SpringTemporaryScopeConfiguration.METADATA_MAP_BEAN_NAME;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(SpringTemporaryScopeConfiguration.class)
class ContextTest {

    @Autowired
    @Qualifier(METADATA_MAP_BEAN_NAME)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    Map<String, TemporaryScopeMetadata> temporaryScopeMetadataMap;
    @Autowired
    TemporaryScopeBeanHolder temporaryScopeBeanHolder;
    @Autowired
    TemporaryScopeMetadataHolder temporaryScopeMetadataHolder;
    @Autowired
    TemporaryScopeProvider temporaryScopeProvider;
    @Autowired
    SpringTemporaryScopeProvider springTemporaryScopeProvider;
    @Autowired
    SpringTemporaryScopeRegistrar springTemporaryScopeRegistrar;
    @Autowired
    SpringTemporaryScopeMetadataRegistrar springTemporaryScopeMetadataRegistrar;

    @Test
    void should_load_context() {
        assertNotNull(temporaryScopeMetadataMap, METADATA_MAP_BEAN_NAME);
        assertNotNull(temporaryScopeBeanHolder, "TemporaryScopeBeanHolder");
        assertNotNull(temporaryScopeMetadataHolder, "TemporaryScopeMetadataHolder");
        assertNotNull(temporaryScopeProvider, "TemporaryScopeProvider");
        assertNotNull(springTemporaryScopeProvider, "SpringTemporaryScopeProvider");
        assertNotNull(springTemporaryScopeRegistrar, "SpringTemporaryScopeRegistrar");
        assertNotNull(springTemporaryScopeMetadataRegistrar, "SpringTemporaryScopeMetadataRegistrar");
    }

}
