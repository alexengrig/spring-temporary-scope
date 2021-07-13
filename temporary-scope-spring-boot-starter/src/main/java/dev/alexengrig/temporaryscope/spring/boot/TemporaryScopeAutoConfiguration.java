package dev.alexengrig.temporaryscope.spring.boot;

import dev.alexengrig.temporaryscope.spring.SpringTemporaryScopeConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringTemporaryScopeConfiguration.class)
public class TemporaryScopeAutoConfiguration {
}
