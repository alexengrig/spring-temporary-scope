package dev.alexengrig.temporaryscope.spring;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static dev.alexengrig.temporaryscope.spring.SpringTemporaryScopeConfiguration.METADATA_MAP_BEAN_NAME;

public class SpringTemporaryScopeInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = Logger.getLogger(SpringTemporaryScopeInitializer.class.getName());

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        LOGGER.info("Start");
        registerMetadataMap(context);
        LOGGER.info("Finished");
    }

    private void registerMetadataMap(ConfigurableApplicationContext context) {
        context.getBeanFactory().registerSingleton(METADATA_MAP_BEAN_NAME, new ConcurrentHashMap<>());
        //FIXME: Replace map with wrapper
        LOGGER.info(() -> "Registered bean of metadata map '" + METADATA_MAP_BEAN_NAME + "' - ConcurrentHashMap");
    }

}
