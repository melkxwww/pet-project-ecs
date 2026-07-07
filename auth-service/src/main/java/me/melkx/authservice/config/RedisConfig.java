package me.melkx.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {
    @Bean
    public RedisScript<Long> registerTokenScript() {
        return RedisScript.of(
                """
                        local tokenKey = KEYS[1]
                        local setKey = KEYS[2]
                        local ttl = tonumber(ARGV[1])
                        redis.call('SET', tokenKey, setKey, 'EX', ttl)
                        redis.call('SADD', setKey, tokenKey)
                        return 1
                        """,
                Long.class
        );
    }
}
