package com.iisi.patrol.webGuard.web.rest;

import com.iisi.patrol.webGuard.service.CommonSSHUtils;
import com.iisi.patrol.webGuard.service.sshService.ConnectionConfig;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api")
public class CheckResource {

    @PostMapping("/check-ssh")
    public String checkSshConnect(@RequestBody ConnectionConfig connectionConfig) throws Exception {
        return CommonSSHUtils.useSshCommand(connectionConfig,"echo 'ok'");
    }

    @PostMapping("/service/check-file")
    public boolean checkFile(@RequestBody String fullFilePathWithFileName) throws Exception {

        File f = new File(fullFilePathWithFileName);
        if(f.exists() && !f.isDirectory()) {
           return true;
        }else{
           return false;
        }
    }
}
