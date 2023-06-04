package com.example.application.usecase;

import com.example.domain.member.dto.MemberDto;
import com.example.domain.member.service.MemberReadService;
import com.example.domain.post.entity.Post;
import com.example.domain.post.service.PostLikeWriteService;
import com.example.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreatePostLikeUsacase {

    private final PostReadService postReadService;
    private final MemberReadService memberReadService;
    private final PostLikeWriteService postLikeWriteService;

    public void execute(Long postId, Long memberId) {
        Post post = postReadService.getPost(postId);
        MemberDto member = memberReadService.getMember(memberId);
        postLikeWriteService.create(post, member);
    }
}
