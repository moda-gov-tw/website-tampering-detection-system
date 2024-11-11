package com.iisi.patrol.webGuard.service.sshService;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConnectionConfig {

    private String serverIp;
    private String userName;
    private String passWord;
    private Integer port;


}
