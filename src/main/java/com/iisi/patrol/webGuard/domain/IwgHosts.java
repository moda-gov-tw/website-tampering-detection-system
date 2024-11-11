package com.iisi.patrol.webGuard.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class IwgHosts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = "pwd", length = 50, nullable = false)
    private String pwd;

    @Size(max = 25)
    @Column(name = "username", length = 25, nullable = false)
    private String username;

    @Size(max = 25)
    @Column(name = "hostname", length = 25, nullable = false)
    private String hostname;

    @Column(name = "port", nullable = false)
    private Integer port;

    @Size(max = 500)
    @Column(name = "mail_receiver", length = 500, nullable = true)
    private String mailReceiver;

    @Size(max = 500)
    @Column(name = "sms_receiver", length = 500, nullable = true)
    private String smsReceiver;

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

    @Size(max = 1)
    @Column(name = "active", length = 1, nullable = true)
    private String active;

}
