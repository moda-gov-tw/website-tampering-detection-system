package com.iisi.patrol.webGuard.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class IwgHostsLogsDTO implements Serializable {

    private String hostname;
    private Integer port;
    private Instant triggerTime;
    private Instant finishTime;
    private String result;
    private String smsStatus;
    private String mailStatus;
    private String createUser;
    private Instant createTime;
    private String updateUser;
    private Instant updateTime;
    private String targetFilename;
}
