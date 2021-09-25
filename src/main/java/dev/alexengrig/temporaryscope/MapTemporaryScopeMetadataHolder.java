package dev.alexengrig.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapTemporaryScopeMetadataHolder implements TemporaryScopeMetadataHolder {

    private final Map<String, TemporaryScopeMetadata> metadataByName;

    public MapTemporaryScopeMetadataHolder() {
        this(new ConcurrentHashMap<>());
    }

    public MapTemporaryScopeMetadataHolder(Map<String, TemporaryScopeMetadata> map) {
        metadataByName = map;
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
