package com.apps.config.cache;


import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheConfig {

    @Bean
    public ApplicationCacheManager initApplicationCacheManager(
            @Autowired(required = false) CacheManager cacheManager,
            @Autowired(required = false) RedisTemplate<Object, Object> redisTemplate,
            @Value("${spring.cache.redis.key-prefix:}") String redisKeyPrefix) {
        return new ApplicationCacheManager(cacheManager, redisTemplate, redisKeyPrefix);
    }



}
