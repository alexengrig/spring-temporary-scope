package dev.alexengrig.temporaryscope.spring;

import java.time.LocalDateTime;

public abstract class WithCreatedAt {

    private final LocalDateTime createdAt = LocalDateTime.now();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
