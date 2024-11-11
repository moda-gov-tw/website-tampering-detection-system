package com.iisi.patrol.webGuard.service.dto.mapper;

import com.iisi.patrol.webGuard.domain.IwgHosts;
import com.iisi.patrol.webGuard.service.dto.IwgHostsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IwgHostsMapper extends EntityMapper<IwgHostsDTO, IwgHosts> {
}
