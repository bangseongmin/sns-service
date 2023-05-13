package com.example.application.controller;

import com.example.application.usecase.CreateFollowMemberUsacase;
import com.example.application.usecase.FindFollowingMembersUsacase;
import com.example.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

    private final CreateFollowMemberUsacase createFollowMemberUsacase;
    private final FindFollowingMembersUsacase findFollowingMembersUsacase;

    @PostMapping("/{fromId}/{toId}")
    public void create(@PathVariable Long fromId, @PathVariable Long toId) {
        createFollowMemberUsacase.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> getFollowings(@PathVariable Long fromId) {
        return findFollowingMembersUsacase.execute(fromId);
    }

}
