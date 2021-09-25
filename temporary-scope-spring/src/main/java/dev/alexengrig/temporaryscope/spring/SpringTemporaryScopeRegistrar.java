package dev.alexengrig.temporaryscope.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.logging.Logger;

import static dev.alexengrig.temporaryscope.spring.SpringTemporaryScopeConfiguration.SCOPE_NAME;

public class SpringTemporaryScopeRegistrar implements BeanFactoryPostProcessor {

    private static final Logger LOGGER = Logger.getLogger(SpringTemporaryScopeRegistrar.class.getName());

    private final SpringTemporaryScopeProvider scopeProvider;

    public SpringTemporaryScopeRegistrar(SpringTemporaryScopeProvider scopeProvider) {
        this.scopeProvider = scopeProvider;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LOGGER.info("Register scope '" + SCOPE_NAME + "'");
        beanFactory.registerScope(SCOPE_NAME, scopeProvider);
    }

}
