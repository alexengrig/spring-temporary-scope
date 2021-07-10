package dev.alexengrig.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SingletonTemporaryScopeMetadataHolder implements TemporaryScopeMetadataHolder {

    private static final Object INSTANCE_LOCK = new Object();
    private static volatile SingletonTemporaryScopeMetadataHolder INSTANCE;

    private final Map<String, TemporaryScopeMetadata> metadataByName = new ConcurrentHashMap<>();

    private SingletonTemporaryScopeMetadataHolder() {
        //TODO FromIs
    }

    public static SingletonTemporaryScopeMetadataHolder instance() {
        SingletonTemporaryScopeMetadataHolder instance = INSTANCE;
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                instance = INSTANCE;
                if (instance == null) {
                    INSTANCE = instance = new SingletonTemporaryScopeMetadataHolder();
                }
            }
        }
        return instance;
    }

    @Override
    public void put(String beanName, TemporaryScopeMetadata scopeMetadata) {
        metadataByName.put(beanName, scopeMetadata);
    }

    @Override
    public TemporaryScopeMetadata get(String beanName) {
        return metadataByName.get(beanName);
    }

}
