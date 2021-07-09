package dev.alexengrig.spring.temporaryscope;

import java.util.function.Supplier;

public interface TemporaryScopeProvider {

    Object get(String beanName, Supplier<Object> beanProducer);

}
