package dev.alexengrig.temporaryscope.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class SpringTemporaryScopeRegistrar implements BeanFactoryPostProcessor {

    private final SpringTemporaryScopeProvider scopeProvider;

    public SpringTemporaryScopeRegistrar(SpringTemporaryScopeProvider scopeProvider) {
        this.scopeProvider = scopeProvider;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope(SpringTemporaryScopeConfiguration.SCOPE_NAME, scopeProvider);
    }

}
