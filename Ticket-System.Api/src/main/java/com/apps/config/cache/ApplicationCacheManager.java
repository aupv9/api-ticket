package com.apps.config.cache;

import com.apps.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ApplicationCacheManager {
    private CacheManager cacheManager;
    private RedisTemplate<Object, Object> redisTemplate;
    private String redisKeyPrefix;

    public ApplicationCacheManager(CacheManager cacheManager,
                                   RedisTemplate<Object, Object> redisTemplate,
                                   String redisKeyPrefix) {
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
        this.redisKeyPrefix = redisKeyPrefix;
    }

    public List<Object> getKeys(String pattern) {
        List<Object> allkeys = new ArrayList<>();
        if (cacheManager instanceof RedisCacheManager) {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            Set<Object> keys = redisTemplate.keys(redisKeyPrefix + "*" + pattern);
            allkeys = new ArrayList<>(keys);
        } else if (cacheManager instanceof ConcurrentMapCacheManager) {
            // ConcurrentMapCacheManager ccmManager = (ConcurrentMapCacheManager) cacheManager;
            for (String name : cacheManager.getCacheNames()) {
                Cache cache = (Cache) cacheManager.getCache(name);
                if (cache instanceof ConcurrentMapCache) {
                    ConcurrentMapCache ccmcache = (ConcurrentMapCache) cache;
                    Set<Map.Entry<Object, Object>> set = ccmcache.getNativeCache().entrySet();
                    List<Object> keys = set.stream().map(Map.Entry::getKey).collect(Collectors.toList());
                    allkeys.addAll(keys);

                }
                if (!CommonUtils.isNullOrEmpty(pattern) && !pattern.equals("*")) {
                    String patternWildcard = pattern.replaceAll("\\*", ".*");
                    allkeys = allkeys.stream().filter(k -> k.toString().matches(patternWildcard)).collect(Collectors.toList());
                }
            }
        } else {
            log.warn("unsupport cacheManager: {}", cacheManager.getClass().getName());
        }

        log.debug("Pattern {} Found {} keys in Cache {}", pattern, allkeys.size(), cacheManager.getClass().getName());
        return allkeys;
    }

    public void evits(List<Object> keys) {
        if (cacheManager instanceof RedisCacheManager) {
            redisTemplate.setKeySerializer(new StringRedisSerializer());

            if (!CommonUtils.isNullOrEmpty(keys)) {
                Long total = redisTemplate.delete(keys);
                log.info("evits redis {} keys success {} keys ", keys.size(), total);
            }

        } else if (cacheManager instanceof ConcurrentMapCacheManager) {
            for (String name : cacheManager.getCacheNames()) {
                Cache cache = (Cache) cacheManager.getCache(name);
                keys.forEach(mkey -> {
                    boolean succeed = cache.evictIfPresent(mkey);
                    if (succeed) {
                        log.debug("remove inmemory at cachename {} with keys {}", name, mkey);
                    }
                });
            }
        } else {
            log.warn("unsupport cacheManager: {}", cacheManager.getClass().getName());
        }
    }

    public void clearCache(String cachename) {
        if (cacheManager == null) {
            log.debug("cacheManager isnull, cancel clearCacheSchedule");
            return;
        }

        Cache cache = cacheManager.getCache(cachename);
        if (cache == null) {
            log.debug("cache {} isnull, cancel clearCacheSchedule", cachename);
            return;
        }

        cache.clear();
        log.debug("Clear cache {} succeed", cachename);
    }

}
