package dev.alexengrig.spring.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentTemporaryScopeBeanHolder implements TemporaryScopeBeanHolder {

    private final Map<String, TemporaryScopeBean> beanByName = new ConcurrentHashMap<>();

    @Override
    public void put(String name, TemporaryScopeBean bean) {
        beanByName.put(name, bean);
    }

    @Override
    public boolean contains(String name) {
        return beanByName.containsKey(name);
    }

    @Override
    public TemporaryScopeBean get(String name) {
        return beanByName.get(name);
    }
}
