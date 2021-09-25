package dev.alexengrig.temporaryscope.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Temporary {

    //TODO: Move below to configuration
    String CLASS_NAME = Temporary.class.getName();
    String VALUE_NAME = "value";
    String UNIT_NAME = "unit";

    long value();

    ChronoUnit unit() default ChronoUnit.MILLIS;

}
