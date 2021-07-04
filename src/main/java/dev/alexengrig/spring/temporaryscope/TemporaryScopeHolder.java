package dev.alexengrig.spring.temporaryscope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemporaryScopeHolder implements Scope {

    private final TemporaryScopeMetadataHolder metadataHolder = TemporaryScopeMetadataHolder.instance();
    private final TemporalStore store = new TemporalStore();
    private final Map<String, Runnable> destructionCallbackByName = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (store.has(name)) {
            TemporalStore.Entry entry = store.get(name);
            if (LocalDateTime.now().isBefore(entry.expirationAt())) {
                return entry.object();
            }
        }
        TemporaryScopeMetadata metadata = metadataHolder.get(name);
        LocalDateTime expirationAt = LocalDateTime.now().plus(metadata.amount(), metadata.unit());
        Object object = objectFactory.getObject();
        return store.put(name, expirationAt, object);
    }

    @Override
    public Object remove(String name) {
        Runnable destructionCallback = destructionCallbackByName.remove(name);
        if (destructionCallback != null) {
            destructionCallback.run();
        }
        return store.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        destructionCallbackByName.put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    private static final class TemporalStore {

        private final Map<String, Entry> entryByName = new ConcurrentHashMap<>();

        public boolean has(String name) {
            return entryByName.containsKey(name);
        }

        public Entry get(String name) {
            return entryByName.get(name);
        }

        public Object put(String name, LocalDateTime expirationAt, Object object) {
            entryByName.put(name, new Entry(expirationAt, object));
            return object;
        }

        public Object remove(String name) {
            Entry entry = entryByName.remove(name);
            return entry.object();
        }

        private static final record Entry(LocalDateTime expirationAt, Object object) {
        }

    }

}
