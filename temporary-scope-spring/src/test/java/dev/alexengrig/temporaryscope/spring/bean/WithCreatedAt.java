package dev.alexengrig.temporaryscope.spring.bean;

import java.time.LocalDateTime;

public abstract class WithCreatedAt {

    private final LocalDateTime createdAt = LocalDateTime.now();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
