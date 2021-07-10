package dev.alexengrig.temporaryscope;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

public record TemporaryScopeMetadata(long amount, TemporalUnit unit) {

    public LocalDateTime createExpiredAt() {
        return createExpiredAt(LocalDateTime.now());
    }

    public LocalDateTime createExpiredAt(LocalDateTime dateTime) {
        return dateTime.plus(amount, unit);
    }

}
