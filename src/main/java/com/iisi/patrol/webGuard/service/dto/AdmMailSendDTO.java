package com.iisi.patrol.webGuard.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class AdmMailSendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String mailType;
    private String sourceId;
    private String sender;
    private String receiver;
    private String subject;
    private String content;
    private Instant expectSendTime;
    private Instant realSendTime;
    private String status;
    private String processMessage;
    private String createUser;
    private Instant createTime;
    private String updateUser;
    private Instant updateTime;
    private String filePath;
    private Boolean isHtml;
}
