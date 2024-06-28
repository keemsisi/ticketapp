package org.core.backend.ticketapp.passport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean storeDataAsString(String id, String stringData, Long expiryInMinutes) {
        stringRedisTemplate.opsForValue().set(id, stringData);
        setExpire(id, expiryInMinutes, TimeUnit.MINUTES);
        return true;
    }

    public String fetchDataAsString(String id) {
        return stringRedisTemplate.opsForValue().get(id);
    }

    public Boolean deleteData(String id) {
        return stringRedisTemplate.delete(id);
    }

    private void setExpire(String key, long expiry, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, expiry, timeUnit);
    }
}
