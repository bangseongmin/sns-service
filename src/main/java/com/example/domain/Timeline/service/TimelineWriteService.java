package com.example.domain.Timeline.service;

import com.example.domain.Timeline.domain.Timeline;
import com.example.domain.Timeline.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineWriteService {

    private final TimelineRepository timelineRepository;

    public void deliveryTimeline(Long postId, List<Long> tomemberIds) {
        List<Timeline> timelines = tomemberIds.stream()
                .map((memberId) -> toTimeline(postId, memberId))
                .toList();

        timelineRepository.bulkInsert(timelines);
    }

    private static Timeline toTimeline(Long postId, Long memberId) {
        return Timeline.builder().memberId(memberId).postId(postId).build();
    }
}
