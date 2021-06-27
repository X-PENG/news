package com.peng.news.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.news.cache.constant.CacheConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存及redis相关配置
 * @author PENG
 * @version 1.0
 * @date 2021/6/20 21:27
 */
@Configuration
public class RedisConfig {

    @Value("${news.cache.prefix}")
    private String cachePrefix = "news::";

    /**
     * Spring Cache的全局默认的缓存失效时间
     */
    @Value("${news.cache.expire}")
    private Integer cacheExpire = 3600;

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate =  new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //JSON序列化器
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        //完成Java对象和JSON互转
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //设置RedisTemplate的序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return  redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);

        jackson2JsonRedisSerializer.setObjectMapper(om);


        //默认配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(cachePrefix)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        //设置了缓存过期时间 的默认配置
        RedisCacheConfiguration defaultConfigWithDefaultTtl = defaultConfig.entryTtl(Duration.ofSeconds(cacheExpire));

        //指定每个缓存的缓存失效时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>(2);
        //设置首页相关数据的缓存的过期时间为2小时
        cacheConfigurations.put(CacheConstants.CACHE_NAME_FRONTEND_INDEX_PAGE, defaultConfig.entryTtl(Duration.ofHours(2)));
        //设置热点新闻缓存的过期时间为6小时
        cacheConfigurations.put(CacheConstants.CACHE_NAME_HOT_NEWS, defaultConfig.entryTtl(Duration.ofHours(6)));
        //设置新闻列表缓存的过期时间为6小时
        cacheConfigurations.put(CacheConstants.CACHE_NAME_FRONTEND_NEWS_LIST, defaultConfig.entryTtl(Duration.ofHours(6)));

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                //设置全局默认配置，根据配置可知：全局默认的缓存实现时间为cacheExpire（秒）
                .cacheDefaults(defaultConfigWithDefaultTtl)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();

        return cacheManager;
    }
}
