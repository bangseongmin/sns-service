package com.example.domain.post.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Post {

    private final Long id;

    private final Long memberId;

    private final String contents;

    private Long likes;

    private final Long version;

    private final LocalDate createdDate;

    private final LocalDateTime createdAt;

    @Builder
    public Post(Long id, Long memberId, String contents, Long likes, Long version, LocalDate createdDate, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.contents = Objects.requireNonNull(contents);
        this.likes = likes == null ? 0 : likes;
        this.version = version == null ? 0 : version;
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public void incrementLike() {
        likes += 1;
    }
}
