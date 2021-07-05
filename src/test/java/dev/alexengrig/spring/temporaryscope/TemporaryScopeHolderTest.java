package dev.alexengrig.spring.temporaryscope;

import dev.alexengrig.spring.temporaryscope.bean.BaseTemporary;
import dev.alexengrig.spring.temporaryscope.bean.TemporaryBean;
import dev.alexengrig.spring.temporaryscope.bean.TemporaryComponent;
import dev.alexengrig.spring.temporaryscope.bean.TemporaryXmlBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(TemporaryScopeHolderTest.Config.class)
class TemporaryScopeHolderTest {

    @Autowired
    ObjectFactory<TemporaryBean> temporaryBeanProvider;
    @Autowired
    ObjectFactory<TemporaryXmlBean> temporaryXmlBeanProvider;
    @Autowired
    ObjectFactory<TemporaryComponent> temporaryComponentProvider;

    static <B extends BaseTemporary> void test(ObjectFactory<? extends B> objectFactory) throws InterruptedException {
        B bean = objectFactory.getObject();
        assertNotNull(bean, "Bean is null");
        TimeUnit.MILLISECONDS.sleep(1000);
        B nextBean = objectFactory.getObject();
        assertNotNull(nextBean, "Next bean is null");
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
    @ComponentScan("dev.alexengrig.spring.temporaryscope.bean")
    @ImportResource("classpath:bean.xml")
    static class Config {

        @Bean
        static TemporaryScopeRegister temporaryScopeRegister() {
            return new TemporaryScopeRegister();
        }

        @Bean
        static TemporaryScopeMetadataRegister temporaryScopeMetadataRegister() {
            return new TemporaryScopeMetadataRegister();
        }

        @Bean
        @TemporaryScope(1000)
        static TemporaryBean temporaryBean() {
            return new TemporaryBean();
        }

    }

}