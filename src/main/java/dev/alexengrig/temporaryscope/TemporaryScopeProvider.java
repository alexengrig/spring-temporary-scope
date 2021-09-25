package dev.alexengrig.temporaryscope;

import java.util.function.Supplier;

public interface TemporaryScopeProvider {

    Object get(String beanName, Supplier<Object> beanProducer);

    @SuppressWarnings("unchecked")
    default <T> T getAs(String beanName, Supplier<? extends T> beanProducer) throws ClassCastException {
        return (T) get(beanName, beanProducer::get);
    }

    Object remove(String beanName);

    @SuppressWarnings("unchecked")
    default <T> T removeAs(String beanName) throws ClassCastException {
        return (T) remove(beanName);
    }

}
