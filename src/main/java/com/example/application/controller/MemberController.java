package com.example.application.controller;

import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.dto.MemberNicknameHistoryDto;
import com.example.domain.member.dto.RegisterMemberCommand;
import com.example.domain.member.service.MemberReadService;
import com.example.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        return memberWriteService.register(command);
    }

    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @PutMapping("/{id}/nickname")
    public MemberDto changeNickname(@PathVariable Long id, @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
    }

    @GetMapping("/{memberId}/nickname-histories")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId) {
        return memberReadService.getNicknameHistories(memberId);
    }


}
