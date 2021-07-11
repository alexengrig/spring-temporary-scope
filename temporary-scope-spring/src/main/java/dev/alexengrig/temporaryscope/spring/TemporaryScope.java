package dev.alexengrig.temporaryscope.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Temporary(0)
@Scope(TemporaryScopeConfiguration.SCOPE_NAME)
public @interface TemporaryScope {

    String CLASS_NAME = TemporaryScope.class.getName();
    String VALUE_NAME = "name";
    String UNIT_NAME = "unit";

    @AliasFor(value = "value", annotation = Temporary.class)
    long value();

    @AliasFor(value = "unit", annotation = Temporary.class)
    ChronoUnit unit() default ChronoUnit.MILLIS;

    @AliasFor(value = "proxyMode", annotation = Scope.class)
    ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;

}
