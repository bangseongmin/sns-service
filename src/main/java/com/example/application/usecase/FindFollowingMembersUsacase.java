package com.example.application.usecase;

import com.example.domain.follow.dto.FollowDto;
import com.example.domain.follow.service.FollowReadService;
import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FindFollowingMembersUsacase {

    private final MemberReadService memberReadService;
    private final FollowReadService followReadService;

    public List<MemberDto> execute(Long memberId) {
        /**
         * 1. fromMemberId = memberId -> Follow List
         * 2. 1번을 순회하면서 회원저보를 찾으면 된다!
         */
        List<FollowDto> folloings = followReadService.getFolloings(memberId);
        List<Long> followingMemberIds = folloings.stream().map(FollowDto::toMemberId).toList();
        return memberReadService.findAllByIdInIds(followingMemberIds);
    }
}
