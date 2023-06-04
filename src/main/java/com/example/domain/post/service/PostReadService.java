package com.example.domain.post.service;

import com.example.domain.post.dto.DailyPostCount;
import com.example.domain.post.dto.DailyPostCountRequest;
import com.example.domain.post.dto.PostDto;
import com.example.domain.post.entity.Post;
import com.example.domain.post.repository.PostLikeRepository;
import com.example.domain.post.repository.PostRepository;
import com.example.util.CursorRequest;
import com.example.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostReadService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

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

    public Post getPost(Long postId) {
        return postRepository.findById(postId, false).orElseThrow();
    }

    public Page<PostDto> getPosts(Long memberId, Pageable Pageable) {
        return postRepository.findAllByMemberId(memberId, Pageable).map(this::fromPost);
    }

    private PostDto fromPost(Post post) {
        return new PostDto(
                post.getId(),
                post.getContents(),
                post.getCreatedAt(),
                postLikeRepository.count(post.getId())
        );
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
        List<Post> posts = findAllByMemberId(memberId, cursorRequest);

        Long nextKey = getNextKey(posts);

        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
        List<Post> posts = findAllByMemberIds(memberIds, cursorRequest);

        Long nextKey = getNextKey(posts);

        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    private List<Post> findAllByMemberId(Long memberId, CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }

        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }

    private List<Post> findAllByMemberIds(List<Long> memberIds, CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(cursorRequest.key(), memberIds, cursorRequest.size());
        }

        return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberIds, cursorRequest.size());
    }

    private static Long getNextKey(List<Post> posts) {
        return posts.stream()
                .mapToLong(Post::getId).min()
                .orElse(CursorRequest.NONE_KEY);
    }

    public List<Post> getPosts(List<Long> ids) {
        return postRepository.findAllByInIds(ids);
    }
}
