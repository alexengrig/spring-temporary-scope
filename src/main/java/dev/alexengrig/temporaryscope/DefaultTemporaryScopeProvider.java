package dev.alexengrig.temporaryscope;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultTemporaryScopeProvider implements TemporaryScopeProvider {

    private final TemporaryScopeBeanHolder beanHolder;
    private final TemporaryScopeMetadataHolder metadataHolder;

    public DefaultTemporaryScopeProvider(TemporaryScopeBeanHolder beanHolder, TemporaryScopeMetadataHolder metadataHolder) {
        this.beanHolder = Objects.requireNonNull(beanHolder, "TemporaryScopeBeanHolder");
        this.metadataHolder = Objects.requireNonNull(metadataHolder, "TemporaryScopeMetadataHolder");
    }

    @Override
    public Object get(String beanName, Supplier<Object> beanProducer) {
        if (beanHolder.contains(beanName)) {
            TemporaryScopeBean wrapper = beanHolder.get(beanName);
            if (wrapper.isNotExpired()) {
                return wrapper.bean();
            }
        }
        TemporaryScopeMetadata metadata = metadataHolder.get(beanName);
        if (metadata == null) {
            throw new IllegalArgumentException("No bean metadata for bean name: " + beanName);
        }
        Object bean = beanProducer.get();
        LocalDateTime expiredAt = metadata.createExpiredAt();
        beanHolder.put(beanName, new TemporaryScopeBean(expiredAt, bean));
        return bean;
    }

    @Override
    public Object remove(String beanName) {
        TemporaryScopeBean wrapper = beanHolder.remove(beanName);
        if (wrapper == null) {
            return null;
        }
        return wrapper.bean();
    }

}
