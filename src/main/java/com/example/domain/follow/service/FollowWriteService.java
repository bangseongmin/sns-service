package com.example.domain.follow.service;

import com.example.domain.follow.entity.Follow;
import com.example.domain.follow.repository.FollowRepository;
import com.example.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@RequiredArgsConstructor
@Service
public class FollowWriteService {

    private final FollowRepository followRepository;

    public void create(MemberDto fromMember, MemberDto toMember) {
        /**
         * from, to 회원 정보 받고 저장
         * from <-> to validate
         */
        Assert.isTrue(!fromMember.id().equals(toMember.id()), "From, To 회원이 동일합니다.");

        Follow follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();

        followRepository.save(follow);
    }
}
