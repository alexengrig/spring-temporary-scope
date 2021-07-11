package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.spring.bean.WithCreatedAt;
import dev.alexengrig.temporaryscope.spring.bean.BeanWithCreatedAt;
import dev.alexengrig.temporaryscope.spring.bean.ComponentWithCreatedAt;
import dev.alexengrig.temporaryscope.spring.bean.XmlBeanWithCreatedAt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(SpringTemporaryScopeTest.Config.class)
class SpringTemporaryScopeTest {

    @Autowired
    ObjectFactory<BeanWithCreatedAt> temporaryBeanProvider;
    @Autowired
    ObjectFactory<XmlBeanWithCreatedAt> temporaryXmlBeanProvider;
    @Autowired
    ObjectFactory<ComponentWithCreatedAt> temporaryComponentProvider;

    static <B extends WithCreatedAt> void test(ObjectFactory<? extends B> objectFactory) throws InterruptedException {
        B bean = objectFactory.getObject();
        assertNotNull(bean, "Bean is null");
        TimeUnit.MILLISECONDS.sleep(1000);
        B nextBean = objectFactory.getObject();
        assertNotNull(nextBean, "Next bean is null");
        assertNotSame(bean, nextBean, "Next bean is same bean");
        assertNotEquals(bean.getCreatedAt(), nextBean.getCreatedAt(), "CreatedAt is not different");
    }

    @Test
    void loadContext() {
        assertNotNull(temporaryBeanProvider, "Provider of TemporaryBean is null");
        assertNotNull(temporaryComponentProvider, "Provider of TemporaryXmlBean is null");
        assertNotNull(temporaryComponentProvider, "Provider of TemporaryComponent is null");
    }

    @Test
    void testBean() throws InterruptedException {
        test(temporaryBeanProvider);
    }

    @Test
    void testXmlBean() throws InterruptedException {
        test(temporaryXmlBeanProvider);
    }

    @Test
    void testComponent() throws InterruptedException {
        test(temporaryComponentProvider);
    }

    @Configuration
    @ComponentScan("dev.alexengrig.temporaryscope.spring.bean")
    @ImportResource("classpath:bean.xml")
    static class Config {

        @Bean
        static SpringTemporaryScopeRegistrar temporaryScopeRegister() {
            return new SpringTemporaryScopeRegistrar();
        }

        @Bean
        static SpringTemporaryScopeMetadataRegistrar temporaryScopeMetadataRegister() {
            return new SpringTemporaryScopeMetadataRegistrar();
        }

        @Bean
        @TemporaryScope(1000)
        static BeanWithCreatedAt temporaryBean() {
            return new BeanWithCreatedAt();
        }

      /*TODO Check without properties
        @Bean
        @Scope("temporary")
        static String bean() {
            return "bean";
        }*/
    }

}