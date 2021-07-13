package dev.alexengrig.temporaryscope.spring.boot;

import dev.alexengrig.temporaryscope.spring.TemporaryScope;
import dev.alexengrig.temporaryscope.spring.boot.bean.Rocket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest(classes = {TemporaryScopeAutoConfiguration.class, SpringBootTemporaryScopeTest.Config.class})
class SpringBootTemporaryScopeTest {

    @Autowired
    ObjectFactory<Rocket> rocketProvider;

    @Test
    void contextLoads() {
        assertNotNull("Provider of Rocket is null", rocketProvider);
    }

    @Test
    void testBean() {
        Rocket bean = rocketProvider.getObject();
        assertNotNull("Bean is null", bean);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        Rocket nextBean = rocketProvider.getObject();
        assertNotNull("Next bean is null", nextBean);
        assertNotEquals("CreatedAt is not different", bean.createdAt(), nextBean.createdAt());
    }

    @Configuration
    static class Config {

        @Bean
        @TemporaryScope(1000)
        static Rocket bean() {
            return new Rocket(LocalDateTime.now());
        }

    }

}
