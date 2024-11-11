package com.iisi.patrol.webGuard.service.dto.mapper;

import com.iisi.patrol.webGuard.domain.IwgHostsLogs;
import com.iisi.patrol.webGuard.service.dto.IwgHostsLogsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IwgHostsLogsMapper extends EntityMapper<IwgHostsLogsDTO, IwgHostsLogs> {
}
