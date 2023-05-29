package com.example.domain.member.service;

import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.dto.RegisterMemberCommand;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.MemberNicknameHistory;
import com.example.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.domain.member.dto.MemberDto.toDto;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;

    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    @Transactional
    public MemberDto register(RegisterMemberCommand command) {
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

        Member saveMember = memberRepository.save(member);

        saveMemberNicknameHistory(saveMember.getId(), saveMember.getNickname());
        return toDto(saveMember);
    }

    @Transactional
    public void changeNickname(Long memberId, String nickname) {
        /**
         * 1. 회원의 이름을 변경
         * 2. 변경 내역을 저장
         */
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.changeNickname(nickname);
        memberRepository.save(member);
        
        saveMemberNicknameHistory(memberId, nickname);
    }

    private void saveMemberNicknameHistory(Long memberId, String nickname) {
        memberNicknameHistoryRepository.save(MemberNicknameHistory.builder()
                .memberId(memberId)
                .nickname(nickname)
                .build());
    }
}
