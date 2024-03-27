package com.example.parking.container;

import com.example.parking.application.parking.ParkingService;
import com.example.parking.application.review.ReviewService;
import com.example.parking.auth.AuthService;
import com.example.parking.config.TestConfig;
import com.example.parking.domain.member.MemberRepository;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@Import({TestConfig.class})
@ActiveProfiles("test")
public abstract class ContainerTest {

    private static final RedisContainer REDIS_CONTAINER;
    private static RedisTemplate<String, String> redisTemplate;

    static {
        REDIS_CONTAINER = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG))
                .withExposedPorts(6379);
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getRedisPort);
    }

    @Autowired
    private MySQLDataCleaner mySQLDataCleaner;

    @Autowired
    protected ParkingService parkingService;

    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected AuthService authService;

    @BeforeAll
    static void setUpBeforeClass() {
        String redisHost = REDIS_CONTAINER.getHost();
        int redisPort = REDIS_CONTAINER.getRedisPort();
        LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        redisConnectionFactory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
    }

    @AfterEach
    void teardown() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.serverCommands().flushAll();
        mySQLDataCleaner.delete();
    }
}
