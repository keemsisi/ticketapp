package org.core.backend.ticketapp.passport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void storeDataAsString(String id, String stringData, Long expiryInMinutes) {
        stringRedisTemplate.opsForValue().set(id, stringData);
        setExpire(id, expiryInMinutes, TimeUnit.MINUTES);
    }

    public String fetchDataAsString(final String id) {
        return stringRedisTemplate.opsForValue().get(id);
    }

    public void deleteData(final String id) {
        stringRedisTemplate.delete(id);
    }

    private void setExpire(final String key, final long expiry, final TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, expiry, timeUnit);
    }

    public void decrease(final String key, final int val) {
        stringRedisTemplate.opsForValue().decrement(key, val);
    }
}
