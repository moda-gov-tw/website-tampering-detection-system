package com.iisi.patrol.webGuard.service;

import com.iisi.patrol.webGuard.domain.IwgHostsLogs;
import com.iisi.patrol.webGuard.repository.IwgHostsLogsRepository;
import com.iisi.patrol.webGuard.service.dto.IwgHostsLogsDTO;
import com.iisi.patrol.webGuard.service.dto.mapper.IwgHostsLogsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IwgHostsLogsService {

    private final IwgHostsLogsMapper iwgHostsLogsMapper;
    private final IwgHostsLogsRepository iwgHostsLogsRepository;


    public IwgHostsLogsService(IwgHostsLogsMapper iwgHostsLogsMapper, IwgHostsLogsRepository iwgHostsLogsRepository) {
        this.iwgHostsLogsMapper = iwgHostsLogsMapper;
        this.iwgHostsLogsRepository = iwgHostsLogsRepository;
    }

    @Transactional
    public IwgHostsLogsDTO saveIwgHostsLogs(IwgHostsLogsDTO iwgHostsLogsDTO) {
        IwgHostsLogs iwgHostsLogs = iwgHostsLogsMapper.toEntity(iwgHostsLogsDTO);
        IwgHostsLogs savedIwgHostsLogs = iwgHostsLogsRepository.save(iwgHostsLogs);
        return iwgHostsLogsMapper.toDto(savedIwgHostsLogs);

    }

    @Transactional
    public IwgHostsLogsDTO writeCheckNormalLog(String hostName, Integer port, Instant triggerTime, Instant finishTime, String targetFileName) {
        IwgHostsLogsDTO newLog = new IwgHostsLogsDTO();
        newLog.setHostname(hostName);
        newLog.setPort(port);
        newLog.setTriggerTime(triggerTime);
        newLog.setFinishTime(finishTime);
        newLog.setTargetFilename(targetFileName);
        newLog.setResult("Y");
        //default的
        newLog.setCreateTime(Instant.now());
        newLog.setCreateUser("system");
        return this.saveIwgHostsLogs(newLog);
    }


    @Transactional
    public IwgHostsLogsDTO writeCheckFailLog(String hostName, Integer port, Instant triggerTime, Instant finishTime, String targetFileName, String smsStatus, String mailStatus) {
        IwgHostsLogsDTO newLog = new IwgHostsLogsDTO();
        newLog.setHostname(hostName);
        newLog.setPort(port);
        newLog.setTriggerTime(triggerTime);
        newLog.setFinishTime(finishTime);
        newLog.setTargetFilename(targetFileName);
        //fail情況下會記錄通知的資訊
        newLog.setResult("N");
        newLog.setSmsStatus(smsStatus);
        newLog.setMailStatus(mailStatus);

        //default的
        newLog.setCreateTime(Instant.now());
        newLog.setCreateUser("system");
        return this.saveIwgHostsLogs(newLog);
    }

    public List<IwgHostsLogsDTO> findiwgHosts(IwgHostsLogsDTO iwgHostsLogsDTO) {
        return iwgHostsLogsRepository.findByResultAndHostname(iwgHostsLogsDTO.getHostname()).stream().map(iwgHostsLogsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<IwgHostsLogsDTO> findTop50IwgHosts() {
        return iwgHostsLogsRepository.findTop50ByResultAndHostname().stream().map(iwgHostsLogsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }


    public List<IwgHostsLogs> find(IwgHostsLogsDTO iwgHostsLogsDTO) {
        return iwgHostsLogsRepository.findAll();
    }
}
