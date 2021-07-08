package dev.alexengrig.spring.temporaryscope;

public interface TemporaryScopeBeanHolder {

    void put(String beanName, Object beanObject);

    Object get(String beanName);

}
