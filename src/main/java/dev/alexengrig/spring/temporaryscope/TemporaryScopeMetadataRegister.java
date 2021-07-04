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

public class TemporaryScopeMetadataRegister implements BeanFactoryPostProcessor {

    private static final String UNSUPPORTED_SCOPE_MESSAGE = "Scope \"%s\" supports only Annotation-based configuration"
            .formatted(TemporaryScope.SCOPE_NAME);

    private final TemporaryScopeMetadataHolder temporaryScopeMetadataHolder = TemporaryScopeMetadataHolder.instance();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (TemporaryScope.SCOPE_NAME.equals(beanDefinition.getScope())) {
                if (beanDefinition instanceof AnnotatedBeanDefinition annotatedBeanDefinition) {
                    AnnotatedTypeMetadata metadata = getMetadata(annotatedBeanDefinition);
                    if (metadata.isAnnotated(TemporaryScope.CLASS_NAME)) {
                        TemporaryScopeMetadata temporaryScopeMetadata = getTemporaryScopeMetadata(metadata);
                        temporaryScopeMetadataHolder.put(beanName, temporaryScopeMetadata);
                    }
                } else {
                    //TODO: Add default metadata with 1 minute and overriding via property
                    throw new UnsupportedOperationException(UNSUPPORTED_SCOPE_MESSAGE);
                }
            }
        }
    }

    private AnnotatedTypeMetadata getMetadata(AnnotatedBeanDefinition annotatedBeanDefinition) {
        AnnotatedTypeMetadata metadata = annotatedBeanDefinition.getFactoryMethodMetadata();
        if (metadata != null) {
            return metadata;
        }
        return annotatedBeanDefinition.getMetadata();
    }

    private TemporaryScopeMetadata getTemporaryScopeMetadata(AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(TemporaryScope.CLASS_NAME);
        TemporaryScopeGetter getter = new TemporaryScopeGetter(attributes);
        long amount = getter.value();
        ChronoUnit unit = getter.unit();
        return new TemporaryScopeMetadata(amount, unit);
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static record TemporaryScopeGetter(Map<String, Object> attributes) implements TemporaryScope {

        @Override
        public long value() {
            Object value = attributes.get("value");
            return (long) value;
        }

        @Override
        public ChronoUnit unit() {
            Object unit = attributes.get("unit");
            return (ChronoUnit) unit;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            throw new UnsupportedOperationException("This is not an annotation!");
        }

    }

}
