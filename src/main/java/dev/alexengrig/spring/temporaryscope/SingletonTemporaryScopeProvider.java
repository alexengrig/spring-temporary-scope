package dev.alexengrig.spring.temporaryscope;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class SingletonTemporaryScopeProvider implements TemporaryScopeProvider {

    private static final Object INSTANCE_LOCK = new Object();
    private static volatile SingletonTemporaryScopeProvider INSTANCE;

    private final TemporaryScopeBeanHolder beanHolder;
    private final TemporaryScopeMetadataHolder metadataHolder;

    private SingletonTemporaryScopeProvider(TemporaryScopeBeanHolder beanHolder,
                                            TemporaryScopeMetadataHolder metadataHolder) {
        //TODO FromIs
        this.beanHolder = beanHolder;
        this.metadataHolder = metadataHolder;
    }

    public static SingletonTemporaryScopeProvider instance() {
        SingletonTemporaryScopeProvider instance = INSTANCE;
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                instance = INSTANCE;
                if (instance == null) {
                    TemporaryScopeBeanHolder beanHolder = SingletonTemporaryScopeBeanHolder.instance();
                    TemporaryScopeMetadataHolder metadataHolder = SingletonTemporaryScopeMetadataHolder.instance();
                    INSTANCE = instance = new SingletonTemporaryScopeProvider(beanHolder, metadataHolder);
                }
            }
        }
        return instance;
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

    @Override
    public Object remove(String beanName) {
        TemporaryScopeBean wrapper = beanHolder.remove(beanName);
        return wrapper.bean();
    }
}
