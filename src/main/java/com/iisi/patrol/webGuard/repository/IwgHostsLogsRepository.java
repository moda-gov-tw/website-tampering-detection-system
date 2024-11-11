package com.iisi.patrol.webGuard.repository;

import com.iisi.patrol.webGuard.domain.IwgHosts;
import com.iisi.patrol.webGuard.domain.IwgHostsLogs;
import com.iisi.patrol.webGuard.service.dto.IwgHostsLogsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IwgHostsLogsRepository extends JpaRepository<IwgHostsLogs, Long> , JpaSpecificationExecutor<IwgHosts> {
    @Query(value = "SELECT * from IWG_HOSTS_LOGS where HOSTNAME LIKE CONCAT ('%',:hostname,'%') order by finish_time desc"
            , nativeQuery = true)
//            "/*APPEND_WHERE*/ " +
//            " AND [RESULT] = :result  " +
//            "/*APPEND_WHERE*/ " +
    List<IwgHostsLogs> findByResultAndHostname(@Param("hostname")String hostname);

    @Query(value = "SELECT TOP 50 * from IWG_HOSTS_LOGS order by finish_time desc"
            , nativeQuery = true)
//            "/*APPEND_WHERE*/ " +
//            " AND [RESULT] = :result  " +
//            "/*APPEND_WHERE*/ " +
    List<IwgHostsLogs> findTop50ByResultAndHostname();


}
