package com.apps.config.cache;


import com.apps.utils.CommonUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@ConditionalOnExpression("'${spring.cache.type}'=='redis' && '${application.redis.cache.custom.enable}'=='true'")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({ CacheProperties.class })
@ConditionalOnClass({ CacheProperties.Redis.class, RedisCacheConfiguration.class })
public class RedisCacheConfig {

    @Bean
    @ConfigurationProperties(prefix = "application.redis.cache")
    public RedisCustomCacheProperties redisCustomCacheProperties() {
        return new RedisCustomCacheProperties();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            @Value("${spring.redis.host}") String redisHost,
            @Value("${spring.redis.port}") int redisPort                           ) {
        // Tạo Standalone Connection tới Redis
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    RedisCacheManager cacheManager(CacheProperties cacheProperties,
                                   RedisCustomCacheProperties redisCustomCacheProperties,
                                   ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration,
                                   ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers,
                                   RedisConnectionFactory redisConnectionFactory,
                                   ResourceLoader resourceLoader) {

        RedisCacheConfiguration defaultConfiguration = determineConfiguration(cacheProperties, redisCacheConfiguration, resourceLoader.getClassLoader());
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(defaultConfiguration);
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }
        redisCacheManagerBuilderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));

        Map<String, RedisCacheConfiguration> map = new HashMap<>();
        Optional.ofNullable(redisCustomCacheProperties)
                .map(RedisCustomCacheProperties::getCustomCache)
                .ifPresent(customCache -> {
                    customCache.forEach((key, cache) -> {
                        RedisCacheConfiguration cfg = handleRedisCacheConfiguration(cacheProperties.getRedis(), cache, defaultConfiguration);
                        map.put(key, cfg);
                    });
                });
        builder.withInitialCacheConfigurations(map);
        return builder.build();
    }

    private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration(
            CacheProperties cacheProperties,
            ObjectProvider<org.springframework.data.redis.cache.RedisCacheConfiguration> redisCacheConfiguration,
            ClassLoader classLoader) {
        return redisCacheConfiguration.getIfAvailable(() -> createConfiguration(cacheProperties, classLoader));
    }

    private org.springframework.data.redis.cache.RedisCacheConfiguration createConfiguration(
            CacheProperties cacheProperties, ClassLoader classLoader) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
                .defaultCacheConfig();
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    private RedisCacheConfiguration handleRedisCacheConfiguration(CacheProperties.Redis defaultRedisProperties, CacheProperties.Redis redisProperties, RedisCacheConfiguration config) {
        if (Objects.isNull(redisProperties)) {
            return config;
        }

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        String keyPrefix = Stream.of(defaultRedisProperties.getKeyPrefix(), redisProperties.getKeyPrefix()).filter(k -> !CommonUtils.isNullOrEmpty(k)).collect(Collectors.joining());

        if (!CommonUtils.isNullOrEmpty(keyPrefix)) {
            config = config.computePrefixWith(cacheName -> keyPrefix + cacheName + "::");
        }
        return config;
    }

}
