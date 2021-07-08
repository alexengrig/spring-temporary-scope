package dev.alexengrig.spring.temporaryscope;

import java.time.LocalDateTime;

public record TemporaryScopeBean(LocalDateTime expiredAt, Object bean) {

    public boolean isExpired() {
        return isExpired(LocalDateTime.now());
    }

    public boolean isNotExpired() {
        return !isExpired();
    }

    public boolean isExpired(LocalDateTime dateTime) {
        return expiredAt.isBefore(dateTime);
    }

    public boolean isNotExpired(LocalDateTime dateTime) {
        return !isExpired(dateTime);
    }

}
