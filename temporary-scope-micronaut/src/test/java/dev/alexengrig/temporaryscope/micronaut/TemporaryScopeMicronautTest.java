package dev.alexengrig.temporaryscope.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Provider;

@MicronautTest
class TemporaryScopeMicronautTest {

//    @Inject
//    String application;

    @Inject
    Provider<String> applicationProvider;

    @Test
    void testItWorks() {

        String actual = applicationProvider.get();
        Assertions.assertNotNull(actual);
    }

    //        @TemporaryScope(1000)
    @TemporaryScope(1000)
    String application() {
        return "application";
    }
}