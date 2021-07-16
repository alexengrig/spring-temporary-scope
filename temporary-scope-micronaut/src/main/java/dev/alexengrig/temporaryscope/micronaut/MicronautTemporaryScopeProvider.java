package dev.alexengrig.temporaryscope.micronaut;

import dev.alexengrig.temporaryscope.SingletonTemporaryScopeProvider;
import dev.alexengrig.temporaryscope.TemporaryScopeProvider;
import io.micronaut.context.BeanResolutionContext;
import io.micronaut.context.LifeCycle;
import io.micronaut.context.scope.CustomScope;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.BeanIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class MicronautTemporaryScopeProvider implements CustomScope<TemporaryScope>,
        LifeCycle<MicronautTemporaryScopeProvider> {

    private final TemporaryScopeProvider scopeProvider = SingletonTemporaryScopeProvider.instance();

    @Override
    public Class<TemporaryScope> annotationType() {
        return TemporaryScope.class;
    }

    @Override
    public <T> T get(BeanResolutionContext resolutionContext,
                     BeanDefinition<T> beanDefinition,
                     BeanIdentifier identifier,
                     Provider<T> provider) {
        String beanName = identifier.toString();
        @SuppressWarnings("unchecked")
        T bean = (T) scopeProvider.get(beanName, provider::get);
        return bean;
    }

    @Override
    public <T> Optional<T> remove(BeanIdentifier identifier) {
        String beanName = identifier.toString();
        @SuppressWarnings("unchecked")
        T bean = (T) scopeProvider.remove(beanName);
        return Optional.ofNullable(bean);
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
