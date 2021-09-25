package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.TemporaryScopeMetadata;
import dev.alexengrig.temporaryscope.TemporaryScopeMetadataHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Function;

public class SpringTemporaryScopeMetadataRegistrar implements BeanFactoryPostProcessor {

    private final TemporaryScopeMetadataHolder scopeMetadataHolder;

    public SpringTemporaryScopeMetadataRegistrar(TemporaryScopeMetadataHolder scopeMetadataHolder) {
        this.scopeMetadataHolder = scopeMetadataHolder;
    }

    private void registerMetadata(String beanName, TemporaryScopeMetadata metadata) {
        scopeMetadataHolder.put(beanName, metadata);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (isTargetScope(beanDefinition)) {
                process(beanName, beanDefinition);
            }
        }
    }

    private boolean isTargetScope(BeanDefinition beanDefinition) {
        return SpringTemporaryScopeConfiguration.SCOPE_NAME.equals(beanDefinition.getScope());
    }

    private void process(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinition instanceof AnnotatedBeanDefinition annotatedBeanDefinition) {
            processAnnotatedBean(beanName, annotatedBeanDefinition);
        } else {
            //TODO check other configuration types
            processXmlBean(beanName, beanDefinition);
        }
    }

    private void processAnnotatedBean(String beanName, AnnotatedBeanDefinition annotatedBeanDefinition) {
        AnnotatedTypeMetadata beanMetadata = getBeanMetadata(annotatedBeanDefinition);
        if (beanMetadata.isAnnotated(Temporary.CLASS_NAME)) {
            TemporaryScopeMetadata scopeMetadata = getScopeMetadata(beanMetadata);
            registerMetadata(beanName, scopeMetadata);
        }
        //TODO @Scope("temporary")
    }

    private AnnotatedTypeMetadata getBeanMetadata(AnnotatedBeanDefinition annotatedBeanDefinition) {
        AnnotatedTypeMetadata metadata = annotatedBeanDefinition.getFactoryMethodMetadata();
        if (metadata != null) {
            return metadata;
        }
        return annotatedBeanDefinition.getMetadata();
    }

    private TemporaryScopeMetadata getScopeMetadata(AnnotatedTypeMetadata beanMetadata) {
        TemporaryScopeValueProvider getter = TemporaryScopeValueProvider.from(beanMetadata);
        long amount = getter.value();
        ChronoUnit unit = getter.unit();
        return new TemporaryScopeMetadata(amount, unit);
    }

    private void processXmlBean(String beanName, BeanDefinition beanDefinition) {
        TemporaryScopeMetadata scopeMetadata = getScopeMetadata(beanDefinition);
        registerMetadata(beanName, scopeMetadata);
    }

    private TemporaryScopeMetadata getScopeMetadata(BeanDefinition beanDefinition) {
        TemporaryScopeValueProvider getter = TemporaryScopeValueProvider.from(beanDefinition::getAttribute);
        long amount = getter.value();
        ChronoUnit unit = getter.unit();
        return new TemporaryScopeMetadata(amount, unit);
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static record TemporaryScopeValueProvider(long value, ChronoUnit unit) implements Temporary {

        public static TemporaryScopeValueProvider from(AnnotatedTypeMetadata beanMetadata) {
            //TODO: Refactor
            Map<String, Object> attributes = beanMetadata.getAnnotationAttributes(CLASS_NAME);
            if (attributes == null) {
                throw new IllegalArgumentException("Bean doesn't have @TemporaryScope annotation.");
            }
            long value = (long) attributes.get(Temporary.VALUE_NAME);
            ChronoUnit unit = (ChronoUnit) attributes.get(Temporary.UNIT_NAME);
            return new TemporaryScopeValueProvider(value, unit);
        }

        public static TemporaryScopeValueProvider from(Function<String, Object> getter) {
            //TODO NPE
            long value = Long.parseLong((String) getter.apply(SpringTemporaryScopeConfiguration.VALUE_PROPERTY));
            ChronoUnit unit = ChronoUnit.valueOf((String) getter.apply(SpringTemporaryScopeConfiguration.UNIT_PROPERTY));
            return new TemporaryScopeValueProvider(value, unit);
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            throw new UnsupportedOperationException("This is not an annotation!");
        }

    }

}
