package com.fattah.config;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CacheEvictScheduler {

    @CacheEvict("apiCache30m")
    @Scheduled(fixedDelayString = "${cache30.delay}")
    public void evictApiCache30M(){
        System.out.println(LocalDateTime.now().toLocalDate()+" _ "+LocalDateTime.now().toLocalTime()+"> Api 30 Min Cache Evicted");
    }

    @CacheEvict("apiCache15m")
    @Scheduled(fixedDelayString = "${cache15.delay}")
    public void evictApiCache15M(){
        System.out.println(LocalDateTime.now().toLocalDate()+" _ "+LocalDateTime.now().toLocalTime()+"> Api 15 Min Cache Evicted");
    }
}
