package com.iisi.patrol.webGuard.repository;

import com.iisi.patrol.webGuard.domain.IwgHostsTarget;
import com.iisi.patrol.webGuard.service.dto.IwgHostsTargetDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IwgHostsTargetRepository extends JpaRepository<IwgHostsTarget, Long> {
    @Query(
            value = "SELECT * FROM IWG_HOSTS_TARGET u WHERE u.HOSTNAME = :hostName and u.PORT = :port and u.ACTIVE = 'Y'",
            nativeQuery = true)
    List<IwgHostsTarget> getIwgHostTargetByHost(@Param("hostName") String hostName,@Param("port") int port);

    @Query(
            value = "SELECT * FROM IWG_HOSTS_TARGET u WHERE u.HOSTNAME = :hostName and u.PORT = :port and u.PROFILE = 'DEV'",
            nativeQuery = true)
    List<IwgHostsTarget> getDevIwgHostTargetByHost(@Param("hostName") String hostName,@Param("port") int port);

    List<IwgHostsTarget> findByHostnameAndPort(String hostname, Integer port);

}
