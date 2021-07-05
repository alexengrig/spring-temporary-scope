package dev.alexengrig.spring.temporaryscope.bean;

import dev.alexengrig.spring.temporaryscope.TemporaryScope;
import org.springframework.stereotype.Component;

@Component
@TemporaryScope(1000)
public class TemporaryComponent extends BaseTemporary {
}
