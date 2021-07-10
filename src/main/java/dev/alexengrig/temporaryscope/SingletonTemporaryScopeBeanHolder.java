package dev.alexengrig.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonTemporaryScopeBeanHolder implements TemporaryScopeBeanHolder {

    private static final Object INSTANCE_LOCK = new Object();
    private static volatile SingletonTemporaryScopeBeanHolder INSTANCE;

    private final Map<String, TemporaryScopeBean> beanByName = new ConcurrentHashMap<>();

    private SingletonTemporaryScopeBeanHolder() {
        //TODO FromIs
    }

    public static SingletonTemporaryScopeBeanHolder instance() {
        SingletonTemporaryScopeBeanHolder instance = INSTANCE;
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                instance = INSTANCE;
                if (instance == null) {
                    INSTANCE = instance = new SingletonTemporaryScopeBeanHolder();
                }
            }
        }
        return instance;
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
