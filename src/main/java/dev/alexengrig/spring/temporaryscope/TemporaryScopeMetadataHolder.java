package dev.alexengrig.spring.temporaryscope;

public interface TemporaryScopeMetadataHolder {

    void put(String beanName, TemporaryScopeMetadata scopeMetadata);

    TemporaryScopeMetadata get(String beanName);

}
