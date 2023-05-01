package com.example.domain.member.service;

import com.example.domain.member.dto.RegisterMemberCommand;
import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;

    public Member create(RegisterMemberCommand command) {
        /**
         * 목표 - 회원정보(이메일, 닉네임, 생년월일) 등록
         *     - 닉네임은 10자를 넘길 수 없다.
         * 파라미터 - memberRegisterCommand
         *
         * val member = Member.of(memberRegisterCommand)
         * memberRepository.save()
         */

        Member member = Member.builder()
                .email(command.email())
                .nickname(command.nickname())
                .birthday(command.birthday())
                .build();

        return memberRepository.save(member);
    }
}
