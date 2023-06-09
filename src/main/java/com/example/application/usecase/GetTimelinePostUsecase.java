package com.example.application.usecase;

import com.example.domain.Timeline.domain.Timeline;
import com.example.domain.Timeline.service.TimelineReadService;
import com.example.domain.follow.dto.FollowDto;
import com.example.domain.follow.service.FollowReadService;
import com.example.domain.post.entity.Post;
import com.example.domain.post.service.PostReadService;
import com.example.util.CursorRequest;
import com.example.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetTimelinePostUsecase {

    private final FollowReadService followReadService;
    private final PostReadService postReadService;
    private final TimelineReadService timelineReadService;

    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
        /**
         * 1. memberId -> follow 조회
         * 2. follow를 바탕으로 게시물 조회
         */
        List<FollowDto> folloings = followReadService.getFolloings(memberId);
        List<Long> followingMemberIds = folloings.stream().map(FollowDto::toMemberId).toList();
        return postReadService.getPosts(followingMemberIds, cursorRequest);
    }

    public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        /**
         * 1. Timeline 조회
         * 2. 1번에 해당하는 게시물을 조회한다.
         */
        PageCursor<Timeline> timelines = timelineReadService.getTimelines(memberId, cursorRequest);
        List<Long> postIds = timelines.contents().stream().map(Timeline::getPostId).toList();
        List<Post> posts = postReadService.getPosts(postIds);

        return new PageCursor<>(timelines.nextCursorRequest(), posts);
    }
}
