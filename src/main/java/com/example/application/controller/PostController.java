package com.example.application.controller;

import com.example.application.usecase.CreateFollowMemberUsacase;
import com.example.application.usecase.CreatePostUsecase;
import com.example.application.usecase.GetTimelinePostUsecase;
import com.example.domain.post.dto.DailyPostCount;
import com.example.domain.post.dto.DailyPostCountRequest;
import com.example.domain.post.dto.PostCommand;
import com.example.domain.post.entity.Post;
import com.example.domain.post.service.PostReadService;
import com.example.domain.post.service.PostWriteService;
import com.example.util.CursorRequest;
import com.example.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final GetTimelinePostUsecase getTimelinePostUsecase;
    private final CreatePostUsecase createPostUsecase;

    @PostMapping
    public Long create(PostCommand command) {
        return createPostUsecase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<Post> getPosts(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        return postReadService.getPosts(memberId, pageable);
    }

    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return getTimelinePostUsecase.executeByTimeline(memberId, cursorRequest);
    }

    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId) {
//        postWriteService.likePost(postId);    비관적 락
        postWriteService.likePostWithOptimisticLock(postId);
    }
}
