package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.spring.bean.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(SpringTemporaryScopeTest.Config.class)
public class SpringTemporaryScopeTest {

    public static final long AMOUNT = 1000;

    @Autowired
    ObjectFactory<TemporaryBean> temporaryBeanProvider;
    @Autowired
    ObjectFactory<TemporaryComponent> temporaryComponentProvider;
    @Autowired
    ObjectFactory<TemporaryScopeBean> temporaryScopeBeanProvider;
    @Autowired
    ObjectFactory<TemporaryScopeComponent> temporaryScopeComponentProvider;
    @Autowired
    ObjectFactory<XmlBean> xmlBeanProvider;

    static <B extends WithCreatedAt> void test(ObjectFactory<? extends B> objectFactory) {
        B bean = objectFactory.getObject();
        assertNotNull(bean, "Bean is null");
        try {
            TimeUnit.MILLISECONDS.sleep(AMOUNT);
        } catch (InterruptedException e) {
            fail(e);
        }
        B nextBean = objectFactory.getObject();
        assertNotNull(nextBean, "Next bean is null");
        assertNotSame(bean, nextBean, "Next bean is same bean");
        assertNotEquals(bean.getCreatedAt(), nextBean.getCreatedAt(), "CreatedAt is not different");
    }

    @Test
    void loadContext() {
        assertNotNull(temporaryBeanProvider, "Provider of TemporaryBean is null");
        assertNotNull(temporaryComponentProvider, "Provider of TemporaryComponent is null");
        assertNotNull(temporaryScopeBeanProvider, "Provider of TemporaryScopeBean is null");
        assertNotNull(temporaryScopeComponentProvider, "Provider of TemporaryScopeComponent is null");
        assertNotNull(xmlBeanProvider, "Provider of XmlBean is null");
    }

    @Test
    void testTemporaryBeanProvider() {
        test(temporaryBeanProvider);
    }

    @Test
    void testTemporaryComponentProvider() {
        test(temporaryComponentProvider);
    }

    @Test
    void testTemporaryScopeBeanProvider() {
        test(temporaryScopeBeanProvider);
    }

    @Test
    void testTemporaryScopeComponentProvider() {
        test(temporaryScopeComponentProvider);
    }

    @Test
    void testXmlBeanProvider() {
        test(xmlBeanProvider);
    }

    @Configuration
    @ComponentScan("dev.alexengrig.temporaryscope.spring.bean")
    @ImportResource("classpath:bean.xml")
    @Import(SpringTemporaryScopeConfiguration.class)
    static class Config {

        @Bean
        @Scope(SpringTemporaryScopeConfiguration.SCOPE_NAME)
        @Temporary(AMOUNT)
        static TemporaryBean temporaryBean() {
            return new TemporaryBean();
        }

        @Bean
        @TemporaryScope(AMOUNT)
        static TemporaryScopeBean temporaryScopeBean() {
            return new TemporaryScopeBean();
        }

    }

}