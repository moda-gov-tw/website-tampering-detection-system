package com.iisi.patrol.webGuard.service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class IwgHostsTargetDTO {

    private long id;
    private String hostname;
    private int port;
    private String originFileLocation;
    private String targetFileLocation;
    private String targetInLocalLocation;
    private String fileName;
    private String active;
    private String originFolder;
    private String targetFolder;
    private String createUser;
    private Instant createTime;
    private String updateUser;
    private Instant updateTime;
}
