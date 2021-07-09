package dev.alexengrig.spring.temporaryscope;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class SimpleTemporaryScopeProvider implements TemporaryScopeProvider {

    private final TemporaryScopeBeanHolder beanHolder;
    private final TemporaryScopeMetadataHolder metadataHolder;

    public SimpleTemporaryScopeProvider(TemporaryScopeBeanHolder beanHolder, TemporaryScopeMetadataHolder metadataHolder) {
        this.beanHolder = beanHolder;
        this.metadataHolder = metadataHolder;
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
        Object bean = beanProducer.get();
        LocalDateTime expiredAt = metadata.createExpiredAt();
        beanHolder.put(beanName, new TemporaryScopeBean(expiredAt, bean));
        return bean;
    }

}
