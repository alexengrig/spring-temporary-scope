package dev.alexengrig.spring.temporaryscope;

import java.util.function.Supplier;

public interface TemporaryScopeProducer {

    Object get(String beanName, Supplier<Object> beanProducer);

}
