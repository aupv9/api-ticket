package com.apps.config.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    
//    @Bean
//    @ConditionalOnExpression("'${application.cache.redis.healthcheck}'=='true'")
//    // @ConditionalOnBean(CacheManager.class)
//    public CacheHealthIndicator initCacheHealthIndicator(CacheManager cacheManager, JedisConnectionFactory jedisConnectionFactory) {
//
//        return new CacheHealthIndicator(cacheManager, jedisConnectionFactory);
//    }

//    @Bean
//    public LockProvider lockProvider(JedisConnectionFactory jedisConnectionFactory, @Value("${spring.cache.redis.key-prefix:}") String redisKeyPrefix) {
//        return new RedisLockProvider(jedisConnectionFactory, redisKeyPrefix);
//    }

}
