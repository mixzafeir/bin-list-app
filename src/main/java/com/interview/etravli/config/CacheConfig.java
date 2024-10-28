package com.interview.etravli.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${SPRING_REDIS_HOST:localhost}")
    private String redisHost;

    @Value("${SPRING_REDIS_PORT:6379}")
    private int redisPort;

    @Value("${SPRING_REDIS_LETTUCE_POOL_MAX_ACTIVE:200}")
    private int maxActive;

    @Value("${SPRING_REDIS_LETTUCE_POOL_MAX_IDLE:50}")
    private int maxIdle;

    @Value("${SPRING_REDIS_LETTUCE_POOL_MIN_IDLE:20}")
    private int minIdle;

    @Value("${SPRING_REDIS_LETTUCE_POOL_MAX_WAIT:5000}")
    private long maxWaitMillis;

    @Value("${SPRING_REDIS_TIMEOUT:5000}")
    private long timeoutMillis;

    @Value("${SPRING_REDIS_LETTUCE_SHUTDOWN_TIMEOUT:100}")
    private long shutdownTimeoutMillis;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();

        RedisCacheConfiguration cardNumberCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(2));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("cardNumberCache", cardNumberCacheConfig);

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        LettucePoolingClientConfiguration poolConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeoutMillis))
                .shutdownTimeout(Duration.ofMillis(shutdownTimeoutMillis))
                .poolConfig(poolConfig())
                .build();

        return new LettuceConnectionFactory(redisConfig, poolConfig);
    }

    private GenericObjectPoolConfig<?> poolConfig() {
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
        return poolConfig;
    }

}
