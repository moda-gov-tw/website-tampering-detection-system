package com.iisi.patrol.webGuard.web.rest;

import com.iisi.patrol.webGuard.service.FileCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CacheController {

    private final Logger log = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    FileCacheService fileCacheService;

    @GetMapping("/service/evictCache")
    public void evictCache(){
        log.info("evictCache");
        fileCacheService.evictFileHashRecordCacheValues();
    }
}
