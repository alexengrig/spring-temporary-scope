package dev.alexengrig.spring.temporaryscope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Function;

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
                        temporaryScopeMetadataHolder.put(beanName, getTemporaryScopeMetadata(metadata));
                    }
                } else if (beanDefinition instanceof GenericBeanDefinition genericBeanDefinition) {
                    TemporaryScopeGetter getter = TemporaryScopeGetter.fromXmlMetaAttributes(beanDefinition::getAttribute);
                    long amount = getter.value();
                    ChronoUnit unit = getter.unit();
                    temporaryScopeMetadataHolder.put(beanName, new TemporaryScopeMetadata(amount, unit));
                } else {
                    //TODO: Add overriding default value
                    temporaryScopeMetadataHolder.put(beanName, new TemporaryScopeMetadata(1000, ChronoUnit.MILLIS));
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
        TemporaryScopeGetter getter = TemporaryScopeGetter.fromAnnotationAttributes(attributes);
        long amount = getter.value();
        ChronoUnit unit = getter.unit();
        return new TemporaryScopeMetadata(amount, unit);
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static record TemporaryScopeGetter(long value, ChronoUnit unit) implements TemporaryScope {

        public static TemporaryScopeGetter fromAnnotationAttributes(Map<String, Object> attributes) {
            long value = (long) attributes.get("value");
            ChronoUnit unit = (ChronoUnit) attributes.get("unit");
            return new TemporaryScopeGetter(value, unit);
        }

        public static TemporaryScopeGetter fromXmlMetaAttributes(Function<String, Object> getter) {
            long value = Long.parseLong((String) getter.apply(TemporaryScope.VALUE_PROPERTY));
            ChronoUnit unit = ChronoUnit.valueOf((String) getter.apply(TemporaryScope.UNIT_PROPERTY));
            return new TemporaryScopeGetter(value, unit);
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            throw new UnsupportedOperationException("This is not an annotation!");
        }

    }

}
