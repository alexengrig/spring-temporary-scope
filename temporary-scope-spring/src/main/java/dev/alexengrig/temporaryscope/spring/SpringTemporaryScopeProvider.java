package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.TemporaryScopeProvider;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringTemporaryScopeProvider implements Scope {

    private final TemporaryScopeProvider provider;
    private final Map<String, Runnable> callbackByName;

    public SpringTemporaryScopeProvider(TemporaryScopeProvider scopeProvider) {
        this(scopeProvider, new ConcurrentHashMap<>());
    }

    public SpringTemporaryScopeProvider(TemporaryScopeProvider scopeProvider, Map<String, Runnable> callbackMap) {
        this.provider = scopeProvider;
        this.callbackByName = callbackMap;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        return provider.get(name, objectFactory::getObject);
    }

    @Override
    public Object remove(String name) {
        Object object = provider.remove(name);
        Runnable callback = callbackByName.get(name);
        if (callback != null) {
            callback.run();
        }
        return object;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        callbackByName.put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

}
