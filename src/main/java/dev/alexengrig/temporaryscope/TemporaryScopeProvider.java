package dev.alexengrig.temporaryscope;

import java.util.function.Supplier;

public interface TemporaryScopeProvider {

    Object get(String beanName, Supplier<Object> beanProducer);

    Object remove(String beanName);

}
