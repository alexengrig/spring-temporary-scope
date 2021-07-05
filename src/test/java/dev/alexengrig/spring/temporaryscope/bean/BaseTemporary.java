package dev.alexengrig.spring.temporaryscope.bean;

import java.time.LocalDateTime;

public abstract class BaseTemporary {

    private final LocalDateTime createdAt = LocalDateTime.now();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
