package dev.alexengrig.spring.temporaryscope;

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

public class TemporaryScopeMetadataRegister implements BeanFactoryPostProcessor {

    private final TemporaryScopeMetadataHolder temporaryScopeMetadataHolder = TemporaryScopeMetadataHolder.instance();

    private void registerMetadata(String beanName, TemporaryScopeMetadata metadata) {
        temporaryScopeMetadataHolder.put(beanName, metadata);
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
        return TemporaryScope.SCOPE_NAME.equals(beanDefinition.getScope());
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
        if (beanMetadata.isAnnotated(TemporaryScope.CLASS_NAME)) {
            TemporaryScopeMetadata scopeMetadata = getScopeMetadata(beanMetadata);
            registerMetadata(beanName, scopeMetadata);
        } else {
            //TODO @Scope("temporary")
        }
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
    private static record TemporaryScopeValueProvider(long value, ChronoUnit unit) implements TemporaryScope {

        public static TemporaryScopeValueProvider from(AnnotatedTypeMetadata beanMetadata) {
            Map<String, Object> attributes = beanMetadata.getAnnotationAttributes(TemporaryScope.CLASS_NAME);
            if (attributes == null) {
                throw new IllegalArgumentException("Bean doesn't have @TemporaryScope annotation.");
            }
            long value = (long) attributes.get(TemporaryScope.VALUE_NAME);
            ChronoUnit unit = (ChronoUnit) attributes.get(TemporaryScope.UNIT_NAME);
            return new TemporaryScopeValueProvider(value, unit);
        }

        public static TemporaryScopeValueProvider from(Function<String, Object> getter) {
            //TODO NPE
            long value = Long.parseLong((String) getter.apply(TemporaryScope.VALUE_PROPERTY));
            ChronoUnit unit = ChronoUnit.valueOf((String) getter.apply(TemporaryScope.UNIT_PROPERTY));
            return new TemporaryScopeValueProvider(value, unit);
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            throw new UnsupportedOperationException("This is not an annotation!");
        }

    }

}
