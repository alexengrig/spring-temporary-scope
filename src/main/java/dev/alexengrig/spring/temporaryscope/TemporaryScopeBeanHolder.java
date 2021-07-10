package dev.alexengrig.spring.temporaryscope;

public interface TemporaryScopeBeanHolder {

    void put(String name, TemporaryScopeBean bean);

    boolean contains(String name);

    TemporaryScopeBean get(String name);

    TemporaryScopeBean remove(String name);

}
