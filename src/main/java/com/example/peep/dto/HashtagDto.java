package com.example.peep.dto;

import com.example.peep.domain.Hashtag;
import com.example.peep.enumType.HashtagType;

public record HashtagDto(
        Long id,
        String content,
        HashtagType hashtag
) {
    public static HashtagDto from(Hashtag hashtag) {
        return new HashtagDto(hashtag.getId(), hashtag.getContent(), hashtag.getType());
    }
}
