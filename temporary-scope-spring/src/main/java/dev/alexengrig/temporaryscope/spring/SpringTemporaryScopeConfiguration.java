package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.TemporaryScopeMetadataHolder;
import org.springframework.context.annotation.Bean;

public class SpringTemporaryScopeConfiguration {

    public static final String SCOPE_NAME = "temporary";

    public static final String VALUE_PROPERTY = Temporary.CLASS_NAME + ".value";

    public static final String UNIT_PROPERTY = Temporary.CLASS_NAME + ".unit";

    @Bean
    static SpringTemporaryScopeRegistrar temporaryScopeRegister(SpringTemporaryScopeProvider scopeProvider) {
        return new SpringTemporaryScopeRegistrar(scopeProvider);
    }

    @Bean
    static SpringTemporaryScopeMetadataRegistrar temporaryScopeMetadataRegister(TemporaryScopeMetadataHolder scopeMetadataHolder) {
        return new SpringTemporaryScopeMetadataRegistrar(scopeMetadataHolder);
    }

}
