package dev.alexengrig.temporaryscope.spring.bean;

import dev.alexengrig.temporaryscope.spring.SpringTemporaryScopeConfiguration;
import dev.alexengrig.temporaryscope.spring.Temporary;
import dev.alexengrig.temporaryscope.spring.TemporaryScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SpringTemporaryScopeConfiguration.SCOPE_NAME)
@Temporary(1000)
public class TemporaryComponent extends WithCreatedAt {
}
