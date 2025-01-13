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

        // Validate the file path
        if (fullFilePathWithFileName.contains("..") || fullFilePathWithFileName.contains("/") || fullFilePathWithFileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file path");
        }

        // Define the base directory
        File baseDir = new File("/safe/directory");

        // Normalize the user-provided path
        File f = new File(baseDir, fullFilePathWithFileName).getCanonicalFile();

        // Ensure the file is within the base directory
        if (!f.getPath().startsWith(baseDir.getCanonicalPath() + File.separator)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        if(f.exists() && !f.isDirectory()) {
           return true;
        }else{
           return false;
        }
    }
}
