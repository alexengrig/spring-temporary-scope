package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.DefaultTemporaryScopeProvider;
import dev.alexengrig.temporaryscope.MapTemporaryScopeBeanHolder;
import dev.alexengrig.temporaryscope.MapTemporaryScopeMetadataHolder;
import dev.alexengrig.temporaryscope.TemporaryScopeBeanHolder;
import dev.alexengrig.temporaryscope.TemporaryScopeMetadata;
import dev.alexengrig.temporaryscope.TemporaryScopeMetadataHolder;
import dev.alexengrig.temporaryscope.TemporaryScopeProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.logging.Logger;

public class SpringTemporaryScopeConfiguration {

    public static final String SCOPE_NAME = "temporary";
    public static final String METADATA_MAP_BEAN_NAME = "temporaryScopeMetadataMap";
    public static final String VALUE_PROPERTY = Temporary.CLASS_NAME + ".value";
    public static final String UNIT_PROPERTY = Temporary.CLASS_NAME + ".unit";

    private static final Logger LOGGER = Logger.getLogger(SpringTemporaryScopeConfiguration.class.getName());

    @Bean
    TemporaryScopeBeanHolder temporaryScopeBeanHolder() {
        LOGGER.info("Create bean of bean holder " +
                    "'temporaryScopeBeanHolder': MapTemporaryScopeBeanHolder");
        return new MapTemporaryScopeBeanHolder(); //TODO: Add with custom map
    }

    @Bean
    TemporaryScopeMetadataHolder temporaryScopeMetadataHolder(
            @Qualifier(METADATA_MAP_BEAN_NAME) Map<String, TemporaryScopeMetadata> temporaryScopeMetadataMap
    ) {
        LOGGER.info(() -> "Create bean of metadata holder " +
                          "'temporaryScopeMetadataHolder' - MapTemporaryScopeMetadataHolder, " +
                          "with bean of metadata map '" + METADATA_MAP_BEAN_NAME + "' - Map<String, TemporaryScopeMetadata>");
        return new MapTemporaryScopeMetadataHolder(
                temporaryScopeMetadataMap
        );
    }

    @Bean
    TemporaryScopeProvider temporaryScopeProvider(
            TemporaryScopeBeanHolder temporaryScopeBeanHolder,
            TemporaryScopeMetadataHolder temporaryScopeMetadataHolder
    ) {
        LOGGER.info("Create bean of scope provider " +
                    "'temporaryScopeProvider' - DefaultTemporaryScopeProvider, " +
                    "with bean of bean holder 'temporaryScopeBeanHolder' - TemporaryScopeBeanHolder " +
                    "and bean of metadata holder 'temporaryScopeMetadataHolder' - TemporaryScopeMetadataHolder");
        return new DefaultTemporaryScopeProvider(
                temporaryScopeBeanHolder,
                temporaryScopeMetadataHolder
        );
    }

    @Bean
    SpringTemporaryScopeProvider springTemporaryScopeProvider(
            TemporaryScopeProvider temporaryScopeProvider
    ) {
        LOGGER.info("Create bean of Spring scope provider " +
                    "'springTemporaryScopeProvider' - SpringTemporaryScopeProvider, " +
                    "with bean of scope provider 'temporaryScopeProvider' - TemporaryScopeProvider");
        return new SpringTemporaryScopeProvider(
                temporaryScopeProvider
        );
    }

    @Bean
    SpringTemporaryScopeRegistrar springTemporaryScopeRegistrar(
            SpringTemporaryScopeProvider springTemporaryScopeProvider
    ) {
        LOGGER.info("Create bean of Spring scope registrar " +
                    "'springTemporaryScopeRegistrar' - SpringTemporaryScopeRegistrar, " +
                    "with bean of Spring scope provider 'springTemporaryScopeProvider' - SpringTemporaryScopeProvider");
        return new SpringTemporaryScopeRegistrar(
                springTemporaryScopeProvider
        );
    }

    @Bean
    SpringTemporaryScopeMetadataRegistrar springTemporaryScopeMetadataRegistrar(
            @Qualifier(METADATA_MAP_BEAN_NAME) Map<String, TemporaryScopeMetadata> temporaryScopeMetadataMap
    ) {
        LOGGER.info("Create bean of Spring scope metadata registrar " +
                    "'springTemporaryScopeMetadataRegistrar' - SpringTemporaryScopeMetadataRegistrar, " +
                    "with bean of metadata map '" + METADATA_MAP_BEAN_NAME + "' - Map<String, TemporaryScopeMetadata>");
        return new SpringTemporaryScopeMetadataRegistrar(
                temporaryScopeMetadataMap
        );
    }

}
