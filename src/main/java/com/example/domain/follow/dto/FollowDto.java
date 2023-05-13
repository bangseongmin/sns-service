package com.example.domain.follow.dto;

import com.example.domain.follow.entity.Follow;

import java.time.LocalDateTime;

public record FollowDto(
        Long id,
        Long fromMemberId,
        Long toMemberId,
        LocalDateTime createdAt
) {

    public static FollowDto toDto(Follow follow) {
        return new FollowDto(follow.getId(), follow.getFromMemberId(), follow.getToMemberId(), follow.getCreatedAt());
    }
}
