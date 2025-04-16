package com.example.peep.service;

import com.example.peep.domain.LoginRecord;
import com.example.peep.domain.enumType.Event;
import com.example.peep.repository.LoginRecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final LoginRecordRepository loginRecordRepository;

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void recordEvent(HttpServletRequest request, String userId, Event eventType) {

        String userAgent = getUserAgent(request);
        String clientIp = getIp(request);

        LoginRecord loginRecord = LoginRecord.of(userId, clientIp, userAgent, eventType);

        loginRecordRepository.save(loginRecord);
    }

    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

}
