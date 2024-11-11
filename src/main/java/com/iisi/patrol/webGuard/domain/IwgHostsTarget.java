package com.iisi.patrol.webGuard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class IwgHostsTarget {

    @Id
//    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 50)
    @Column(name = "hostname", length = 50, nullable = true)
    private String hostname;

//    @NotNull
    @Column(name = "port", length = 4, nullable = false)
    private int port;

    @Size(max = 100)
    @Column(name = "origin_file_location", length = 100, nullable = true)
    private String originFileLocation;

    @Size(max = 100)
    @Column(name = "target_file_location", length = 100, nullable = true)
    private String targetFileLocation;

    @Size(max = 100)
    @Column(name = "target_in_local_location", length = 100, nullable = true)
    private String targetInLocalLocation;

    @Size(max = 100)
    @Column(name = "file_name", length = 100, nullable = true)
    private String fileName;

    @Size(max = 1)
    @Column(name = "active", length = 1, nullable = true)
    private String active;

    @Size(max = 100)
    @Column(name = "origin_folder", length = 100, nullable = true)
    private String originFolder;

    @Size(max = 100)
    @Column(name = "target_folder", length = 100, nullable = true)
    private String targetFolder;

    @Size(max = 25)
    @Column(name = "create_user", length = 25)
    private String createUser;
    @Column(name = "create_time", nullable = true)
    private Instant createTime;
    @Size(max = 25)
    @Column(name = "update_user", length = 25, nullable = true)
    private String updateUser;
    @Column(name = "update_time", nullable = true)
    private Instant updateTime;

}
