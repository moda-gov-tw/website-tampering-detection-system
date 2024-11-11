package com.iisi.patrol.webGuard.domain;


import lombok.Data;

@Data
public class HostProperty {

    String hostName;
    String userName;
    String password;
    int port;
}
