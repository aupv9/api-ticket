package com.apps.config.cache;

import com.apps.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ApplicationCacheManager {

    private final CacheManager cacheManager;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final String redisKeyPrefix;


    public List<Object> getKeys(String pattern) {
        List<Object> allkeys = new ArrayList<>();
        if (cacheManager instanceof RedisCacheManager) {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            Set<Object> keys = redisTemplate.keys(redisKeyPrefix + "*" + pattern);
            log.info("Key redis {}",keys);
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
            log.info("evits redis keys size  {}  ", keys.size());
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
