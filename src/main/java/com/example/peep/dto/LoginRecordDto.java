package com.example.peep.dto;

import com.example.peep.domain.LoginRecord;
import com.example.peep.enumType.Event;

public record LoginRecordDto(
        String userId,
        String clientIp,
        String userAgent,
        Event eventType
) {

    public static  LoginRecordDto from(LoginRecord loginRecord) {
        return new LoginRecordDto(
                loginRecord.getUserId(),
                loginRecord.getClientIp(),
                loginRecord.getUserAgent(),
                loginRecord.getEventType()
        );
    }

    public LoginRecord toEntity() {
        return LoginRecord.of(userId, clientIp, userAgent, eventType);
    }

}
