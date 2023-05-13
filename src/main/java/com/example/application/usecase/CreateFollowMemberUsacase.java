package com.example.application.usecase;

import com.example.domain.follow.service.FollowWriteService;
import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateFollowMemberUsacase {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;

    public void execute(Long fromMemberId, Long toMemberId) {
        /**
         * 1. 입력받은 memberId로 회원 조회
         * 2. FollowWriteService.create()
         */

        MemberDto fromMember = memberReadService.getMember(fromMemberId);
        MemberDto toMember = memberReadService.getMember(toMemberId);

        followWriteService.create(fromMember, toMember);
    }
}
