package com.example.domain.post.service;

import com.example.domain.post.dto.PostCommand;
import com.example.domain.post.entity.Post;
import com.example.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostWriteService {

    private final PostRepository postRepository;

    public Long create(PostCommand command) {
        Post post = Post
                .builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();

        return postRepository.save(post).getId();
    }

    /**
     * 비관적 락
     */
    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId, true).orElseThrow();
        post.incrementLike();
        postRepository.save(post);
    }

    /**
     * 낙관적 락
     */
    public void likePostWithOptimisticLock(Long postId) {
        Post post = postRepository.findById(postId, false).orElseThrow();
        post.incrementLike();
        postRepository.save(post);
    }
}
