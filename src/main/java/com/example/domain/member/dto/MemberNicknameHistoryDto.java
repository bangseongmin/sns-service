package com.example.domain.member.dto;

import com.example.domain.member.entity.MemberNicknameHistory;

import java.time.LocalDateTime;

public record MemberNicknameHistoryDto (
        Long id,
        Long memberId,
        String nickname,
        LocalDateTime createdAt
) {

    public static MemberNicknameHistoryDto toDto(MemberNicknameHistory history) {
        return new MemberNicknameHistoryDto(history.getId(), history.getMemberId(), history.getNickname(), history.getCreatedAt());
    }
}
