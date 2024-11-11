package com.iisi.patrol.webGuard.web.rest;

import com.iisi.patrol.webGuard.domain.IwgHosts;
import com.iisi.patrol.webGuard.domain.IwgHostsLogs;
import com.iisi.patrol.webGuard.service.IwgHostsService;
import com.iisi.patrol.webGuard.service.dto.IwgHostsDTO;
import com.iisi.patrol.webGuard.service.dto.IwgHostsLogsDTO;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IwgHostsController {

    private final IwgHostsService iwgHostsService;

    public IwgHostsController(IwgHostsService iwgHostsService) {
        this.iwgHostsService = iwgHostsService;
    }

    @PostMapping("/create/iwgHosts")
    public void createIwgHosts(@Valid @RequestBody IwgHostsDTO iwgHostsDTO) {
        iwgHostsService.save(iwgHostsDTO);
    }

    @PostMapping("/find/iwgHosts/all")
    public List<IwgHosts> findIwgHosts() {
        return iwgHostsService.findAll();
    }

    @PostMapping("/find/iwgHosts")
    public List<IwgHosts> findByHostname(@Valid @RequestBody IwgHostsDTO iwgHostsDTO) {
        return iwgHostsService.findByHostname(iwgHostsDTO);
    }

    @PostMapping("/update/iwgHosts")
    public void updateIwgHosts(@Valid @RequestBody IwgHostsDTO iwgHostsDTO) {
        //iwgHostsService.update(iwgHostsDTO);
    }

    @PostMapping("/update/iwgHost")
    public void updateIwgHost(@Valid @RequestBody IwgHostsDTO iwgHostsDTO, Authentication auth) {
        iwgHostsService.updateOne(iwgHostsDTO, auth.getName());
    }

    @PostMapping("/add/iwgHost")
    public void addIwgHost(@Valid @RequestBody IwgHostsDTO iwgHostsDTO, Authentication auth) {
        iwgHostsService.addOne(iwgHostsDTO, auth.getName());
    }

    @PostMapping("/find-by-id/iwgHost")
    public IwgHostsDTO findById(@RequestBody Map<String,String> idMap) {
        return iwgHostsService.findById(Long.parseLong(idMap.get("id")));
    }
}
