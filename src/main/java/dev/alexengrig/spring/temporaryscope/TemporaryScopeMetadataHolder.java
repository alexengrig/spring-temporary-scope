package dev.alexengrig.spring.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TemporaryScopeMetadataHolder {

    private static final Object INSTANCE_LOCK = new Object();
    private static volatile TemporaryScopeMetadataHolder INSTANCE;

    private final Map<String, TemporaryScopeMetadata> metadataByName = new ConcurrentHashMap<>();

    private TemporaryScopeMetadataHolder() {
    }

    public static TemporaryScopeMetadataHolder instance() {
        TemporaryScopeMetadataHolder instance = INSTANCE;
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                instance = INSTANCE;
                if (instance == null) {
                    INSTANCE = instance = new TemporaryScopeMetadataHolder();
                }
            }
        }
        return instance;
    }

    public TemporaryScopeMetadata get(String name) {
        return metadataByName.get(name);
    }

    public void put(String name, TemporaryScopeMetadata metadata) {
        metadataByName.put(name, metadata);
    }

}
