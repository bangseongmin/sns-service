package com.example.application.usecase;

import com.example.domain.Timeline.service.TimelineWriteService;
import com.example.domain.follow.dto.FollowDto;
import com.example.domain.follow.service.FollowReadService;
import com.example.domain.post.dto.PostCommand;
import com.example.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand postCommand) {
        Long postId = postWriteService.create(postCommand);

        List<Long> followersMemberIds = followReadService.getFollowers(postCommand.memberId())
                .stream()
                .map(FollowDto::fromMemberId)
                .toList();

        timelineWriteService.deliveryTimeline(postId, followersMemberIds);

        return postId;
    }
}
