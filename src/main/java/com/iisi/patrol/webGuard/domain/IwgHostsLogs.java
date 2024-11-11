package com.iisi.patrol.webGuard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class IwgHostsLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "hostname", length = 25, nullable = false)
    private String hostname;

    @Column(name = "port", length = 4, nullable = false)
    private Integer port;

    @Column(name = "trigger_time", nullable = false)
    private Instant triggerTime;

    @Column(name = "finish_time", nullable = false)
    private Instant finishTime;

    @Size(max = 2)
    @Column(name = "result", length = 2, nullable = true)
    private String result;

    @Size(max = 500)
    @Column(name = "sms_status", length = 500, nullable = true)
    private String smsStatus;

    @Size(max = 500)
    @Column(name = "mail_status", length = 500, nullable = true)
    private String mailStatus;

    @Size(max = 25)
    @Column(name = "create_user", length = 25, nullable = true)
    private String createUser;

    @Column(name = "create_time", nullable = true)
    private Instant createTime;

    @Size(max = 25)
    @Column(name = "update_user", length = 25, nullable = true)
    private String updateUser;

    @Column(name = "update_time", nullable = true)
    private Instant updateTime;

    @Size(max = 50)
    @Column(name = "target_filename", length = 50, nullable = true)
    private String targetFilename;

}
