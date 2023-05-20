package com.example.util;

import java.util.Objects;

public record CursorRequest(Long key, int size) {

    public static final Long NONE_KEY = -1L;

    public Boolean hasKey() {
        return !Objects.isNull(key);
    }

    public CursorRequest next(Long key) {
        return new CursorRequest(key, size);
    }
}
