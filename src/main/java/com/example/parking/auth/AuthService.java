package com.example.parking.auth;

import com.example.parking.auth.authcode.AuthCodeCategory;
import com.example.parking.auth.authcode.AuthCodePlatform;
import com.example.parking.auth.authcode.InValidAuthCodeException;
import com.example.parking.auth.authcode.application.dto.AuthCodeCertificateRequest;
import com.example.parking.auth.authcode.application.dto.AuthCodeRequest;
import com.example.parking.auth.authcode.event.AuthCodeCreateEvent;
import com.example.parking.auth.session.MemberSession;
import com.example.parking.auth.session.MemberSessionRepository;
import com.example.parking.util.authcode.AuthCodeGenerator;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final Long DURATION_MINUTE = 30L;

    private final MemberSessionRepository memberSessionRepository;
    private final AuthCodeGenerator authCodeGenerator;
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

    @Transactional(readOnly = true)
    public MemberSession findSession(String sessionId) {
        return memberSessionRepository.findBySessionIdAndExpiredAtIsGreaterThanEqual(sessionId,
                        LocalDateTime.now())
                .orElseThrow(() -> new UnAuthorizationException("존재하지 않는 sessionId 입니다.", sessionId));
    }

    @Transactional
    public void updateSessionExpiredAt(MemberSession session) {
        session.updateExpiredAt(LocalDateTime.now().plusMinutes(DURATION_MINUTE));
        memberSessionRepository.save(session);
    }

    @Transactional
    public String createAuthCode(AuthCodeRequest authCodeRequest) {
        String destination = authCodeRequest.getDestination();
        AuthCodePlatform authCodePlatform = AuthCodePlatform.find(authCodeRequest.getAuthPlatform());
        AuthCodeCategory authCodeCategory = AuthCodeCategory.find(authCodeRequest.getAuthCodeCategory());

        String randomAuthCode = authCodeGenerator.generateAuthCode();
        String authCodeKey = generateAuthCodeKey(destination, authCodePlatform, authCodeCategory);
        redisTemplate.opsForValue().set(authCodeKey, "true");

        applicationEventPublisher.publishEvent(
                new AuthCodeCreateEvent(
                        destination,
                        randomAuthCode,
                        authCodePlatform.getPlatform(),
                        authCodeCategory.getCategoryName()
                )
        );
        return randomAuthCode;
    }

    @Transactional
    public void certificateAuthCode(AuthCodeCertificateRequest authCodeCertificateRequest) {
        String destination = authCodeCertificateRequest.getDestination();
        AuthCodePlatform authCodePlatform = AuthCodePlatform.find(authCodeCertificateRequest.getAuthCodePlatform());
        AuthCodeCategory authCodeCategory = AuthCodeCategory.find(authCodeCertificateRequest.getAuthCodeCategory());

        String authCodeKey = generateAuthCodeKey(destination, authCodePlatform, authCodeCategory);
        String findResult = redisTemplate.opsForValue().getAndDelete(authCodeKey);
        if (findResult == null) {
            throw new InValidAuthCodeException("존재하지 않는 인증코드 입니다.");
        }
    }

    private String generateAuthCodeKey(String destination, AuthCodePlatform authCodePlatform,
                                       AuthCodeCategory authCodeCategory) {
        StringJoiner stringJoiner = new StringJoiner(":");
        return stringJoiner.add(destination).add(authCodePlatform.getPlatform())
                .add(authCodeCategory.getCategoryName()).toString();
    }
}
