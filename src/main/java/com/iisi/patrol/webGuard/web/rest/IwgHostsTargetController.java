package com.iisi.patrol.webGuard.web.rest;

import com.iisi.patrol.webGuard.service.IwgHostsService;
import com.iisi.patrol.webGuard.service.IwgHostsTargetService;
import com.iisi.patrol.webGuard.service.dto.IwgHostsDTO;
import com.iisi.patrol.webGuard.service.dto.IwgHostsTargetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IwgHostsTargetController {


    private static final Logger log = LoggerFactory.getLogger(IwgHostsTargetController.class);
    private final IwgHostsTargetService iwgHostsTargetService;
    private final IwgHostsService iwgHostsService;

    public IwgHostsTargetController(IwgHostsTargetService iwgHostsTargetService, IwgHostsService iwgHostsService) {
        this.iwgHostsTargetService = iwgHostsTargetService;
        this.iwgHostsService = iwgHostsService;
    }

    @PostMapping("/find-targets-by-hostname-and-port")
    public List<IwgHostsTargetDTO> addIwgHost(@RequestBody Map<String,String> idMap) {
        IwgHostsDTO host = iwgHostsService.findById(Long.parseLong(idMap.get("id")));
        return iwgHostsTargetService.findByHostnameAndPort(host.getHostname(),host.getPort());
    }

    @PostMapping("/iwg-hosts-target/find-by-id")
    public IwgHostsTargetDTO findById(@RequestBody Map<String,String> idMap) {
        return iwgHostsTargetService.findById(Long.parseLong(idMap.get("id")));
    }

    @PostMapping("/iwg-hosts-target/add")
    public IwgHostsTargetDTO addNewIwgHostsTarget(@RequestBody IwgHostsTargetDTO dto, Authentication auth) {
        return iwgHostsTargetService.addIwgHostsTarget(dto,auth.getName());
    }

    @PostMapping("/iwg-hosts-target/update")
    public IwgHostsTargetDTO updateIwgHostsTarget(@RequestBody IwgHostsTargetDTO dto, Authentication auth) {
        return iwgHostsTargetService.updateIwgHostsTarget(dto,auth.getName());
    }

}
