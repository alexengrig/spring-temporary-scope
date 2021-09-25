package dev.alexengrig.temporaryscope.spring;

import dev.alexengrig.temporaryscope.spring.bean.TemporaryBean;
import dev.alexengrig.temporaryscope.spring.bean.TemporaryComponent;
import dev.alexengrig.temporaryscope.spring.bean.TemporaryScopeBean;
import dev.alexengrig.temporaryscope.spring.bean.TemporaryScopeComponent;
import dev.alexengrig.temporaryscope.spring.bean.WithCreatedAt;
import dev.alexengrig.temporaryscope.spring.bean.XmlBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

@SpringJUnitConfig(
        value = {SpringTemporaryScopeConfiguration.class, SpringTemporaryScopeTest.BeanConfiguration.class},
        initializers = SpringTemporaryScopeInitializer.class
)
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
    static class BeanConfiguration {

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