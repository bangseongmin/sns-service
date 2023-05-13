package com.example.domain.follow.service;

import com.example.domain.follow.dto.FollowDto;
import com.example.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowReadService {

    private final FollowRepository followRepository;

    public List<FollowDto> getFolloings(Long memberId) {
        return followRepository.findAllByFromMemberId(memberId)
                .stream()
                .map(FollowDto::toDto)
                .collect(Collectors.toList());
    }
}
