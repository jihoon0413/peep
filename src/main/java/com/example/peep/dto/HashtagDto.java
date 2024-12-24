package com.example.peep.dto;

import com.example.peep.domain.Hashtag;
import com.example.peep.domain.enumType.HashtagType;

public record HashtagDto(
        Long id,
        String content,
        HashtagType hashtag
) {
    public static HashtagDto of(Long id, String content, HashtagType type) {
        return new HashtagDto(id, content, type);
    }

    public static HashtagDto from(Hashtag hashtag) {
        return new HashtagDto(hashtag.getId(), hashtag.getContent(), hashtag.getType());
    }
}
