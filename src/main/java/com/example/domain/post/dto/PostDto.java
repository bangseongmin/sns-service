package com.example.domain.post.dto;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String contents,
        LocalDateTime createdAt,
        Long likes
) {

}
