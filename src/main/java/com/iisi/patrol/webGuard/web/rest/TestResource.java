package com.iisi.patrol.webGuard.web.rest;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.iisi.patrol.webGuard.service.*;
import com.iisi.patrol.webGuard.service.dto.IwgHostsDTO;
import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Profile("dev")
@RestController
@RequestMapping("/api")
public class TestResource {
    private final Logger log = LoggerFactory.getLogger(TestResource.class);

    @Autowired
    ScheduledTaskServiceTest scheduledTaskServiceTest;
    @Autowired
    ScheduledTaskService scheduledTaskService;
    @Autowired
    IwgHostsService iwgHostsService;
    @Autowired
    FileComparisonService fileComparisonService;
    @Autowired
    FileCacheService fileCacheService;

    @GetMapping("/testResource")
    public String testResource(){
        log.info("check resource");
        return "get resource";
    }

    @GetMapping("/service/testResource")
    public String testSecurityResource(){
        log.info("check security resource");
        return "get security resource";
    }

    @GetMapping("/service/testFileSizeCompare")
    public void testFileSizeCompare(){
        scheduledTaskServiceTest.doDevFileComparison();
    }

    @GetMapping("/service/testMapper")
    public IwgHostsDTO testMapper(){
        return iwgHostsService.findById(1l);
    }

    public MapDifference<String,String> checkIsMapEqual(Map<String, String> origin, Map<String, String> fromServer) {
        return Maps.difference(origin, fromServer);
    }

    @GetMapping("/service/doDevFileComparisonInMd5")
    public void doDevFileComparisonInMd5() throws JSchException, IOException {
        scheduledTaskServiceTest.doDevFileComparisonInMd5();
    }

    @GetMapping("/service/doDevFileComparison")
    public void doFileComparisonInMd5() throws JSchException, IOException {
        scheduledTaskService.doFileComparisonInMd5();
    }

    @GetMapping("/service/testCache")
    public Map<String, String> getOriginFolderFilesAndMd5Map(){
        return fileCacheService.getOriginFolderFilesAndMd5Map("C:\\welcome-content");
    }
}
