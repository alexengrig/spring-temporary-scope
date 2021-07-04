package dev.alexengrig.spring.temporaryscope;

import java.time.temporal.TemporalUnit;

public record TemporaryScopeMetadata(long amount, TemporalUnit unit) {
}
