package com.iisi.patrol.webGuard.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class IwgHostsDTO implements Serializable {


    private String password;
    private String username;
    private String hostname;
    private Integer port;
    private String mailReceiver;
    private String smsReceiver;
    private String createUser;
    private Instant createTime;
    private String updateUser;
    private Instant updateTime;
    private String targetFilename;
    private String active;


    private Long id;
    private String fileName;
    private String originFileLocation;
    private String targetFileLocation;
    private String originFolder;
    private String targetFolder;
    private String targetInLocalLocation;
}
