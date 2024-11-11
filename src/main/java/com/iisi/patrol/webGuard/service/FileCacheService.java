package com.iisi.patrol.webGuard.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileCacheService {
    private final Logger log = LoggerFactory.getLogger(FileCacheService.class);

    @Cacheable("fileHashRecord")
    public Map<String, String> getOriginFolderFilesAndMd5Map(String originFolderName) {
        log.info("call without cache");
        return Stream.of(Objects.requireNonNull(new File(originFolderName).listFiles()))
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toMap(File::getName, x -> {
                    String checksum = "";
                    try {
                        byte[] data = new byte[0];
                        data = Files.readAllBytes(Paths.get(x.getPath()));
                        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
                        checksum = new BigInteger(1, hash).toString(16);
                        checksum = StringUtils.leftPad(checksum, 32, '0');
                    } catch (IOException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    return checksum;
                }));
    }


    @CacheEvict(value = "fileHashRecord",  allEntries = true)
    public void evictFileHashRecordCacheValues() {}
}
