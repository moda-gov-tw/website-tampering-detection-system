package com.iisi.patrol.webGuard.service.dto.mapper;

import com.iisi.patrol.webGuard.domain.IwgHostsTarget;
import com.iisi.patrol.webGuard.service.dto.IwgHostsTargetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IwgHostsTargetMapper extends EntityMapper<IwgHostsTargetDTO, IwgHostsTarget> {
}
