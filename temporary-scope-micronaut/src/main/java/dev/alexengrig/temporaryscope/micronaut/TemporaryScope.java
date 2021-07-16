package dev.alexengrig.temporaryscope.micronaut;


import javax.inject.Scope;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Scope
//@Bean
public @interface TemporaryScope {

    long value();

    ChronoUnit unit() default ChronoUnit.MILLIS;

}
