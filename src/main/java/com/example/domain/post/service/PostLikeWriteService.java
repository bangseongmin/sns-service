package com.example.domain.post.service;

import com.example.domain.member.dto.MemberDto;
import com.example.domain.post.entity.Post;
import com.example.domain.post.entity.PostLike;
import com.example.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostLikeWriteService {

    private final PostLikeRepository postLikeRepository;


    public Long create(Post post, MemberDto memberDto) {
        PostLike postLike = PostLike
                .builder()
                .memberId(memberDto.id())
                .postId(post.getId())
                .build();

        return postLikeRepository.save(postLike).getPostId();
    }

}
