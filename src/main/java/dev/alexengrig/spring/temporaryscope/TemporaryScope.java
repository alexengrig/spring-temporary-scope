package dev.alexengrig.spring.temporaryscope;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Scope("temporary")
public @interface TemporaryScope {

    String SCOPE_NAME = "temporary";
    String CLASS_NAME = TemporaryScope.class.getName();

    long value();

    ChronoUnit unit() default ChronoUnit.MILLIS;

}
