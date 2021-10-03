package com.apps.config.cache;

import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.util.Map;

@Data
public class RedisCustomCacheProperties {
    private Map<String, CacheProperties.Redis> customCache;
}
