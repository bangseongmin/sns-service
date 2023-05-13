package com.example.domain.member.service;

import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.dto.MemberNicknameHistoryDto;
import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.domain.member.dto.MemberDto.toDto;

@RequiredArgsConstructor
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    public MemberDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return toDto(member);
    }

    public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
        return memberNicknameHistoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(MemberNicknameHistoryDto::toDto)
                .collect(Collectors.toList());
    }

    public List<MemberDto> findAllByIdInIds(List<Long> ids) {
        if(ids.isEmpty()) return List.of();

        List<Member> members = memberRepository.findAllByIdInIds(ids);

        return members.stream()
                .map(MemberDto::toDto)
                .collect(Collectors.toList());
    }

}
