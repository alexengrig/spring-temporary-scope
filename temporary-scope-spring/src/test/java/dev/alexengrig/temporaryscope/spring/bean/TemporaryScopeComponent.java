package dev.alexengrig.temporaryscope.spring.bean;

import dev.alexengrig.temporaryscope.spring.TemporaryScope;
import org.springframework.stereotype.Component;

@Component
@TemporaryScope(1000)
public class TemporaryScopeComponent extends WithCreatedAt {
}
