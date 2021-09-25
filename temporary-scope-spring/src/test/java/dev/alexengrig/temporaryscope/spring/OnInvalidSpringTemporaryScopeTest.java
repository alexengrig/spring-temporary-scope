package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.spring.invalidbean.BeanWithDuplicateScope;
import dev.alexengrig.temporaryscope.spring.invalidbean.BeanWithoutScope;
import dev.alexengrig.temporaryscope.spring.invalidbean.BeanWithoutTemporary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(
        value = {SpringTemporaryScopeConfiguration.class, OnInvalidSpringTemporaryScopeTest.InvalidBeanConfiguration.class},
        initializers = SpringTemporaryScopeInitializer.class
)
public class OnInvalidSpringTemporaryScopeTest {

    public static final long AMOUNT = 1000;

    @Autowired
    ObjectFactory<BeanWithoutTemporary> beanWithoutTemporaryProvider;
    @Autowired
    ObjectFactory<BeanWithoutScope> beanWithoutScopeProvider;

    @Test
    void should() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                beanWithoutTemporaryProvider.getObject());
        assertEquals("No bean metadata for bean name: beanWithoutTemporary", exception.getMessage(), "Message");
        BeanWithoutScope bean = beanWithoutScopeProvider.getObject();
    }

    @Configuration
    @ComponentScan("dev.alexengrig.temporaryscope.spring.invalidbean")
    @ImportResource("classpath:bean-invalid.xml")
    static class InvalidBeanConfiguration {

        @Bean
        @Scope(SpringTemporaryScopeConfiguration.SCOPE_NAME)
        // Without @Temporary(AMOUNT)
        static BeanWithoutTemporary beanWithoutTemporary() {
            return new BeanWithoutTemporary();
        }

        @Bean
        // Without @Scope(SpringTemporaryScopeConfiguration.SCOPE_NAME)
        @Temporary(AMOUNT)
        static BeanWithoutScope beanWithoutScope() {
            return new BeanWithoutScope();
        }

        //        @Bean
//        @Scope(SpringTemporaryScopeConfiguration.SCOPE_NAME)
//        @TemporaryScope(AMOUNT)
//        TODO
        static BeanWithDuplicateScope beanWithDuplicateScope() {
            return new BeanWithDuplicateScope();
        }

    }

}
