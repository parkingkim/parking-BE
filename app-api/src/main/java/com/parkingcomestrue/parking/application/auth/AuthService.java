package com.parkingcomestrue.parking.application.auth;

import com.parkingcomestrue.common.domain.session.MemberSession;
import com.parkingcomestrue.common.domain.session.repository.MemberSessionRepository;
import com.parkingcomestrue.parking.application.auth.authcode.AuthCodeCategory;
import com.parkingcomestrue.parking.application.auth.authcode.AuthCodePlatform;
import com.parkingcomestrue.parking.application.auth.authcode.AuthCodeValidator;
import com.parkingcomestrue.parking.application.auth.authcode.dto.AuthCodeCertificateRequest;
import com.parkingcomestrue.parking.application.auth.authcode.dto.AuthCodeCreateEvent;
import com.parkingcomestrue.parking.application.auth.authcode.dto.AuthCodeRequest;
import com.parkingcomestrue.parking.application.auth.authcode.util.AuthCodeGenerator;
import com.parkingcomestrue.parking.application.auth.authcode.util.AuthCodeKeyConverter;
import com.parkingcomestrue.parking.support.exception.ClientException;
import com.parkingcomestrue.parking.support.exception.ClientExceptionInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final Long DURATION_MINUTE = 30L;

    @Value("${authcode.expired-time}")
    private Long authCodeExpired;

    private final MemberSessionRepository memberSessionRepository;
    private final AuthCodeGenerator authCodeGenerator;
    private final AuthCodeValidator authCodeValidator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public String createSession(Long memberId) {
        LocalDateTime current = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();

        MemberSession memberSession = new MemberSession(uuid, memberId, current, current.plusMinutes(DURATION_MINUTE));
        memberSessionRepository.save(memberSession);
        return memberSession.getSessionId();
    }

    @Transactional
    public void findAndUpdateSession(String sessionId) {
        MemberSession session = findSession(sessionId);
        session.updateExpiredAt(LocalDateTime.now().plusMinutes(DURATION_MINUTE));
        memberSessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public MemberSession findSession(String sessionId) {
        return memberSessionRepository.findBySessionIdAndExpiredAtIsGreaterThanEqual(sessionId,
                        LocalDateTime.now())
                .orElseThrow(() -> new ClientException(ClientExceptionInformation.UNAUTHORIZED));
    }

    @Transactional
    public String createAuthCode(AuthCodeRequest authCodeRequest) {
        String destination = authCodeRequest.getDestination();
        AuthCodePlatform authCodePlatform = AuthCodePlatform.find(authCodeRequest.getAuthPlatform());
        AuthCodeCategory authCodeCategory = AuthCodeCategory.find(authCodeRequest.getAuthCodeCategory());

        authCodeValidator.validate(authCodePlatform, destination);
        String randomAuthCode = authCodeGenerator.generateAuthCode();
        String authCodeKey = AuthCodeKeyConverter.convert(randomAuthCode, destination, authCodePlatform.getPlatform(),
                authCodeCategory.getCategoryName());
        redisTemplate.opsForValue().set(authCodeKey, randomAuthCode, authCodeExpired, TimeUnit.SECONDS);

        publishAuthCodeCreateEvent(destination, authCodePlatform, authCodeCategory, randomAuthCode);
        return randomAuthCode;
    }

    private void publishAuthCodeCreateEvent(String destination, AuthCodePlatform authCodePlatform,
                                            AuthCodeCategory authCodeCategory, String randomAuthCode) {
        applicationEventPublisher.publishEvent(
                new AuthCodeCreateEvent(
                        destination,
                        randomAuthCode,
                        authCodePlatform.getPlatform(),
                        authCodeCategory.getCategoryName()
                )
        );
    }

    @Transactional
    public void certificateAuthCode(AuthCodeCertificateRequest authCodeCertificateRequest) {
        String authCode = authCodeCertificateRequest.getAuthCode();
        String destination = authCodeCertificateRequest.getDestination();
        AuthCodePlatform authCodePlatform = AuthCodePlatform.find(authCodeCertificateRequest.getAuthCodePlatform());
        AuthCodeCategory authCodeCategory = AuthCodeCategory.find(authCodeCertificateRequest.getAuthCodeCategory());

        String authCodeKey = AuthCodeKeyConverter.convert(authCode, destination, authCodePlatform.getPlatform(),
                authCodeCategory.getCategoryName());
        String findResult = redisTemplate.opsForValue().getAndDelete(authCodeKey);
        if (findResult == null) {
            throw new ClientException(ClientExceptionInformation.INVALID_AUTH_CODE);
        }
    }
}
