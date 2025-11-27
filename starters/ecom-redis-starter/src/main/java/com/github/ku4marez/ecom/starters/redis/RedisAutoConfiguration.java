package com.github.ku4marez.ecom.starters.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@AutoConfiguration
@EnableCaching
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "ecom.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties props) {
        return new LettuceConnectionFactory(props.getHost(), props.getPort());
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(connectionFactory);

        var serializer = jacksonSerializer();

        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setHashKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(serializer);
        tpl.setHashValueSerializer(serializer);
        tpl.afterPropertiesSet();
        return tpl;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisConnectionFactory factory, RedisProperties props) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(props.getTtlSeconds()))
            .prefixCacheNameWith(props.getKeyPrefix() + "::")
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer())
            );

        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleKeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    private RedisSerializer<Object> jacksonSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}
