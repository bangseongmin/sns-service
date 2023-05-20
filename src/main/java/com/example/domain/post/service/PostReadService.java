package com.example.domain.post.service;

import com.example.domain.post.dto.DailyPostCount;
import com.example.domain.post.dto.DailyPostCountRequest;
import com.example.domain.post.entity.Post;
import com.example.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostReadService {

    private final PostRepository postRepository;

    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request) {
        /**
         * 반환 값 -> 리스트[작성일자, 작성회원, 작성 게시물 횟수]
         *
         * 방법 1: Group By
         * SELECT createdDate, memberId, count(id)
         * FROM POST
         * WHERE memberId = :memberId and createdDate between firstDate and lastDate
         * GROUP BY createdDate memberId
         */

        return postRepository.groupByCreatedDate(request);
    }

    public Page<Post> getPosts(Long memberId, Pageable Pageable) {
        return postRepository.findAllByMemberId(memberId, Pageable);
    }
}
