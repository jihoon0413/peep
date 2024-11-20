package com.example.peep.dto;

import com.example.peep.domain.Photo;

public record PhotoDto(
        String photoUrl
) {
    public static PhotoDto of(String photoUrl) {
        return new PhotoDto(photoUrl);
    }

    public static PhotoDto from(Photo photo) {
        return PhotoDto.of(photo.getPhotoUrl());
    }


    public Photo toEntity() {
        return Photo.of(
                photoUrl
        );
    }

}
