package dev.alexengrig.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapTemporaryScopeBeanHolder implements TemporaryScopeBeanHolder {

    private final Map<String, TemporaryScopeBean> beanByName;

    public MapTemporaryScopeBeanHolder() {
        this(new ConcurrentHashMap<>());
    }

    public MapTemporaryScopeBeanHolder(Map<String, TemporaryScopeBean> map) {
        beanByName = map;
    }

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

    @Override
    public TemporaryScopeBean remove(String name) {
        return beanByName.remove(name);
    }

}
