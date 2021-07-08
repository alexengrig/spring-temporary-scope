package dev.alexengrig.spring.temporaryscope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentTemporaryScopeBeanHolder implements TemporaryScopeBeanHolder {

    private final Map<String, Object> objectByName = new ConcurrentHashMap<>();

    @Override
    public void put(String beanName, Object beanObject) {
        objectByName.put(beanName, beanObject);
    }

    @Override
    public Object get(String beanName) {
        return objectByName.get(beanName);
    }

}
