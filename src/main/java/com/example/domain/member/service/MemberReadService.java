package com.example.domain.member.service;

import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.domain.member.dto.MemberDto.toDto;

@RequiredArgsConstructor
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;

    public MemberDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return toDto(member);
    }
}
