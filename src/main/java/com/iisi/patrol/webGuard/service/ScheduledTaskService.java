package com.iisi.patrol.webGuard.service;

import com.iisi.patrol.webGuard.service.dto.IwgHostsDTO;
import com.iisi.patrol.webGuard.service.dto.IwgHostsTargetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledTaskService {

    private final IwgHostsService iwgHostsService;

    private final IwgHostsTargetService iwgHostsTargetService;

    private final FileComparisonService fileComparisonService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskService.class);

    public ScheduledTaskService(IwgHostsService iwgHostsService, IwgHostsTargetService iwgHostsTargetService, FileComparisonService fileComparisonService) {
        this.iwgHostsService = iwgHostsService;
        this.iwgHostsTargetService = iwgHostsTargetService;
        this.fileComparisonService = fileComparisonService;
    }

    /*
       每5分鐘進行檔案比對
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void doFileComparisonInMd5() {
        List<IwgHostsDTO> hostList = iwgHostsService.findActive();
        hostList.forEach(iwgHostsDTO -> {
            log.info("========== start file compare =============================");
            log.info("current host : {}", iwgHostsDTO.getHostname());
            List<IwgHostsTargetDTO> iwgHostsTargetDTOs = iwgHostsTargetService.getIwgHostTargetByHost(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort());
            log.info("check IwgHostsTargetLength : {}", iwgHostsTargetDTOs.size());
            fileComparisonService.fileCompareInMD5ByHostAndTargetList(iwgHostsDTO, iwgHostsTargetDTOs);
        });
        log.info("========== end  file  compare =============================");
    }
}
