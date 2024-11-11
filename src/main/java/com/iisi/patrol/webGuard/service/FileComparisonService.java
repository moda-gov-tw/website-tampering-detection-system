package com.iisi.patrol.webGuard.service;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.iisi.patrol.webGuard.service.dto.IwgHostsDTO;
import com.iisi.patrol.webGuard.service.dto.IwgHostsTargetDTO;
import com.iisi.patrol.webGuard.service.enums.FileHashStatusEnums;
import com.iisi.patrol.webGuard.service.enums.FileSizeStatusEnums;
import com.iisi.patrol.webGuard.service.sshService.ConnectionConfig;
import com.jcraft.jsch.JSchException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

@Service
public class FileComparisonService {
    private static final Logger log = LoggerFactory.getLogger(FileComparisonService.class);

    private final IwgHostsLogsService iwgHostsLogsService;

    private final IwgMailSendService iwgMailSendService;

    private final FileCacheService fileCacheService;

    public FileComparisonService(IwgHostsLogsService iwgHostsLogsService, IwgMailSendService iwgMailSendService, FileCacheService fileCacheService) {
        this.iwgHostsLogsService = iwgHostsLogsService;
        this.iwgMailSendService = iwgMailSendService;
        this.fileCacheService = fileCacheService;
    }


    public void fileCompareByHostAndTargetList(IwgHostsDTO iwgHostsDTO, List<IwgHostsTargetDTO> iwgHostsTargetDTOs) {
        for (IwgHostsTargetDTO targetDTO : iwgHostsTargetDTOs) {
            //取得要監控的file資訊
            String fileName = targetDTO.getFileName();
            String serverLocation = targetDTO.getTargetFileLocation();
            String fromServerLocation = targetDTO.getTargetInLocalLocation();
            String originLocation = targetDTO.getOriginFileLocation();

            log.info("check file name :{}", fileName);
            log.info("fromServerLocation path :{}", fromServerLocation);
            log.info("originLocation path :{}", originLocation);

            ConnectionConfig connectionConfig = new ConnectionConfig(iwgHostsDTO.getHostname(), iwgHostsDTO.getUsername(), PassWordEncodeUtils.decodePassword(iwgHostsDTO.getPassword()), iwgHostsDTO.getPort());
            try {
                Instant triggerTime = Instant.now();
                FileSizeStatusEnums fileStatus = this.compareSizeByCommand(connectionConfig, fileName, serverLocation, originLocation);
                Instant finishTime = Instant.now();

                switch (fileStatus) {
                    case FILE_SIZE_SAME:
                        log.info("check size of {} is normal", fileName);
                        iwgHostsLogsService.writeCheckNormalLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, serverLocation + fileName);
                        break;
                    case FILE_SIZE_DIFF:
                        // 1:寫log 2.替換檔案 3.寄信
                        log.warn("{} size is different", fileName);
                        iwgHostsLogsService.writeCheckFailLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, serverLocation + fileName, iwgHostsDTO.getSmsReceiver(), iwgHostsDTO.getMailReceiver());
                        CommonSSHUtils.useScpCopyLocalFileToRemote(connectionConfig, originLocation, serverLocation, fileName);
                        log.info("exchange diff file {} finish", fileName);
                        StringBuilder sb = new StringBuilder();
                        sb.append("主機:").append(iwgHostsDTO.getHostname()).append(",檔案路徑:").append(serverLocation + fileName).append(",該檔案有異動");
                        sb.append("\n並以替換為iwg主機中的正確版本");
                        iwgMailSendService.saveAdmMailWithReceiverAndContent(iwgHostsDTO.getMailReceiver(), sb.toString());
                        break;
                    case FILE_NOT_EXIST:
                        // 1:寫log 2.放上檔案 3.寄信
                        log.warn("{} is not exist", fileName);
                        iwgHostsLogsService.writeCheckFailLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, serverLocation + fileName, iwgHostsDTO.getSmsReceiver(), iwgHostsDTO.getMailReceiver());
                        CommonSSHUtils.useScpCopyLocalFileToRemote(connectionConfig, originLocation, serverLocation, fileName);
                        log.info("upload file {} finish", fileName);
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append("主機:").append(iwgHostsDTO.getHostname()).append(",檔案路徑:").append(serverLocation + fileName).append(",該檔案不存在");
                        sb1.append("\n並上傳主機中的正確版本");
                        iwgMailSendService.saveAdmMailWithReceiverAndContent(iwgHostsDTO.getMailReceiver(), sb1.toString());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FileSizeStatusEnums compareSizeByCommand(ConnectionConfig connectionConfig, String fileName, String serverLocation, String originLocation) throws Exception {

        String response = CommonSSHUtils.useSshCommand(connectionConfig, "du -b " + serverLocation + fileName);
        log.info(response);
        if (StringUtils.isNotBlank(response) && response.contains("No such file or directory")) {
            return FileSizeStatusEnums.FILE_NOT_EXIST;
        }
        String serverFileSize = response.split("\t")[0];
        Long localFileSize = new File(originLocation + fileName).length();
        int intLocalFileSize = localFileSize.intValue();
        int intServerFileSize = Integer.parseInt(serverFileSize);
        return intLocalFileSize == intServerFileSize ? FileSizeStatusEnums.FILE_SIZE_SAME : FileSizeStatusEnums.FILE_SIZE_DIFF;
    }


    /**
     * MD5 比對
     */
    public void fileCompareInMD5ByHostAndTargetList(IwgHostsDTO iwgHostsDTO, List<IwgHostsTargetDTO> iwgHostsTargetDTOs) {
        //一個host一個連線資訊
        ConnectionConfig connectionConfig = new ConnectionConfig(iwgHostsDTO.getHostname(), iwgHostsDTO.getUsername(), PassWordEncodeUtils.decodePassword(iwgHostsDTO.getPassword()), iwgHostsDTO.getPort());
        //針對host所有的target做比對
        for (IwgHostsTargetDTO targetDTO : iwgHostsTargetDTOs) {
            //看target是否是單一檔案 或是 一個目錄
            if (StringUtils.isNotBlank(targetDTO.getFileName())) {
                String fileName = targetDTO.getFileName();
                String serverLocation = targetDTO.getTargetFileLocation();
                String originLocation = targetDTO.getOriginFileLocation();

                Instant triggerTime = Instant.now();
                FileHashStatusEnums fileStatus;
                //fileStatus 取得比對後的狀態(正常/不正常)
                try {
                    fileStatus = this.compareSingleFileMd5ByCommand(connectionConfig, fileName, serverLocation, originLocation);
                    Instant finishTime = Instant.now();
                    //針對fileStatus狀態做不同處理
                    this.handleSingleFileComparisonResult(fileStatus, iwgHostsDTO, connectionConfig, originLocation, serverLocation, fileName, triggerTime, finishTime);
                } catch (JSchException | NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
                //如果不是單一檔案就看是否為整個目錄
            } else if (StringUtils.isNotBlank(targetDTO.getTargetFolder())) {
                if (!(targetDTO.getTargetFolder().endsWith("\\") || targetDTO.getTargetFolder().endsWith("/"))) {
                    targetDTO.setTargetFolder(targetDTO.getTargetFolder() + File.separator);
                }
                //取得target folder 檔名對md5 String的map
                Instant triggerTime = Instant.now();
                try {
                    Map<String, String> serverFilesMap = this.getServerFolderFilesAndMd5Map(connectionConfig, targetDTO.getTargetFolder());
                    Map<String, String> originFilesMap = fileCacheService.getOriginFolderFilesAndMd5Map(targetDTO.getOriginFolder());
                    //                    log.info("check fileDiff map");
                    //                    for(Map.Entry<String, String> map : serverFilesMap.entrySet()){
                    //                        log.info("key: {}, value:{}",map.getKey(),map.getValue());
                    //                    }
                    //
                    //                    log.info("check originOnly map");
                    //                    for(Map.Entry<String,String> map : originFilesMap.entrySet()){
                    //                        log.info("key: {}, value:{}",map.getKey(),map.getValue());
                    //                    }
                    //比對這兩個map
                    MapDifference<String, String> diff = Maps.difference(originFilesMap, serverFilesMap);

                    /**
                     * fileDiff  :兩個相同檔名,但md5不同
                     * originOnly:只有origin有
                     * serverOnly:只有server有
                     */
                    Map<String, MapDifference.ValueDifference<String>> fileDiff = diff.entriesDiffering();
                    Map<String, String> originOnly = diff.entriesOnlyOnLeft();
                    Map<String, String> serverOnly = diff.entriesOnlyOnRight();

                    //for(Map.Entry<String, MapDifference.ValueDifference<String>> map : fileDiff.entrySet()){
                    //    log.info("key: {}, value:{}",map.getKey(),map.getValue());
                    //}
                    if (fileDiff.size() > 0 || originOnly.size() > 0 || serverOnly.size()>0) {
                        /*
                         *比對有誤的時候,新增一個紀錄所有問題的map,並拿這個map生成訊息
                         *預計會寫成
                         *  fileDiff    檔案差異
                         *  fileLost    檔案缺漏
                         *  fileUnknown 不知名檔案
                        */
                        Map<String,List<String>> messageMap = new HashMap<>();
                        List<String> fileDiffList = new ArrayList<>();
                        List<String> fileLostList = new ArrayList<>();
                        List<String> fileUnknownList = new ArrayList<>();
                        log.warn("check folder abnormal");
                        //origin server 不一樣的狀況,先備份server的,再把origin的蓋掉server的
                        //create backup folder
                        CommonSSHUtils.useSshCommand(connectionConfig, this.createEmptyBackupFolderCommand(targetDTO.getTargetFolder()));
                        for (Map.Entry<String, MapDifference.ValueDifference<String>> map : fileDiff.entrySet()) {
                            String needUpdateFileName = map.getKey();
                            //處理 fileDiff,把origin裡面的檔案搬過去到serverLocation
                            //組backup 不一樣file的字串
                            String serverFileFullLocation = targetDTO.getTargetFolder() + needUpdateFileName;
                            StringBuilder backupFolderPathBuilder = new StringBuilder();
                            if (targetDTO.getTargetFolder().endsWith("/") || targetDTO.getTargetFolder().endsWith("\\")) {
                                backupFolderPathBuilder.append(targetDTO.getTargetFolder().substring(0, targetDTO.getTargetFolder().length() - 1));
                                backupFolderPathBuilder.append("_bk").append("/");
                            }
                            String serverFileFullBackupLocation = backupFolderPathBuilder.toString();
                            String mvCommand = "mv " + serverFileFullLocation + " " + serverFileFullBackupLocation;
                            CommonSSHUtils.useSshCommand(connectionConfig, mvCommand);
                            //backup完copy origin to server
                            CommonSSHUtils.useScpCopyLocalFileToRemote(connectionConfig, targetDTO.getOriginFolder(), targetDTO.getTargetFolder(), needUpdateFileName);
                            fileDiffList.add(needUpdateFileName);//message用,紀錄有差異的檔案
                        }
                        messageMap.put("fileDiff",fileDiffList);

                        //只有origin有
                        for (Map.Entry<String, String> map : originOnly.entrySet()) {
                            String needUpdateFileName = map.getKey();
                            //處理 fileDiff,把origin裡面的檔案搬過去到serverLocation
                            CommonSSHUtils.useScpCopyLocalFileToRemote(connectionConfig, targetDTO.getOriginFolder(), targetDTO.getTargetFolder(), needUpdateFileName);
                            fileLostList.add(needUpdateFileName);//message用,紀錄origin有but server沒有的檔案
                        }
                        messageMap.put("fileLost",fileLostList);

                        //只有server有
                        for (Map.Entry<String, String> map : serverOnly.entrySet()) {
                            String needDeleteFileName = map.getKey();
                            //1.備份
                            String serverFileFullLocation = targetDTO.getTargetFolder() + needDeleteFileName;
                            StringBuilder backupFolderPathBuilder = new StringBuilder();
                            if (targetDTO.getTargetFolder().endsWith("/") || targetDTO.getTargetFolder().endsWith("\\")) {
                                backupFolderPathBuilder.append(targetDTO.getTargetFolder().substring(0, targetDTO.getTargetFolder().length() - 1));
                                backupFolderPathBuilder.append("_bk").append("/");
                            }
                            String serverFileFullBackupLocation = backupFolderPathBuilder.toString();
                            String mvCommand = "mv " + serverFileFullLocation + " " + serverFileFullBackupLocation;
                            CommonSSHUtils.useSshCommand(connectionConfig, mvCommand);
                            //2.刪除
                            log.info("remove file : {}{}", targetDTO.getTargetFolder(), needDeleteFileName);
                            CommonSSHUtils.useSshCommand(connectionConfig, "rm -f " + targetDTO.getTargetFolder() + needDeleteFileName);
                            fileUnknownList.add(needDeleteFileName);//message用,紀錄有未在origin的檔案
                        }
                        messageMap.put("fileUnknown",fileUnknownList);

                        //寫log
                        Instant finishTime = Instant.now();
                        iwgHostsLogsService.writeCheckFailLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, targetDTO.getTargetFolder(), iwgHostsDTO.getSmsReceiver(), iwgHostsDTO.getMailReceiver());
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append("主機:").append(iwgHostsDTO.getHostname()).append(",檔案目錄:").append(targetDTO.getTargetFolder()).append(",該目錄比對有誤");
                        //詳細的紀錄
                        if(messageMap.get("fileDiff") != null && messageMap.get("fileDiff").size()>0){
                            sb1.append("\n檔案差異:");
                            String fileDiffs = String.join(",", messageMap.get("fileDiff"));
                            sb1.append(fileDiffs);
                        }
                        if(messageMap.get("fileLost") != null && messageMap.get("fileLost").size()>0){
                            sb1.append("\n檔案缺漏:");
                            String fileLost = String.join(",", messageMap.get("fileLost"));
                            sb1.append(fileLost);
                        }
                        if(messageMap.get("fileUnknown") != null && messageMap.get("fileUnknown").size()>0 ){
                            sb1.append("\n不知名檔案:");
                            String fileUnknown = String.join(",", messageMap.get("fileUnknown"));
                            sb1.append(fileUnknown);
                        }
                        sb1.append("\n已處理並上傳主機中的正確版本");
                        iwgMailSendService.saveAdmMailWithReceiverAndContent(iwgHostsDTO.getMailReceiver(), sb1.toString());
                    } else {
                        //normal
                        Instant finishTime = Instant.now();
                        iwgHostsLogsService.writeCheckNormalLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, targetDTO.getTargetFolder());
                        log.info("check folder {} normal",targetDTO.getTargetFolder());
                    }
                } catch (JSchException | IOException e) {
                    e.printStackTrace();
                }

            } else {
                //skip
            }


        }
    }

    private String createEmptyBackupFolderCommand(String serverLocation){
        StringBuilder commandBuilder = new StringBuilder();
        //-m 設定mod
        //-p 處理folder 已存在的問題
        commandBuilder.append("mkdir -m 766 -p ");
        if(serverLocation.endsWith("/")|| serverLocation.endsWith("\\")){
            serverLocation = serverLocation.substring(0,serverLocation.length()-1);
        }
        commandBuilder.append(serverLocation).append(" ");
        commandBuilder.append(serverLocation).append("_bk");
        return commandBuilder.toString();
    }


    /**
     *
     * @param connectionConfig 連線資訊
     * @param fileName  檔名
     * @param serverLocation server檔案位置
     * @param originLocation 本地的檔案位置
     * @return FILE_HASH_SAME(比對正常),FILE_HASH_DIFF(檔案不同), FILE_NOT_EXIST(檔案不存在)
     * @throws JSchException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private FileHashStatusEnums compareSingleFileMd5ByCommand(ConnectionConfig connectionConfig, String fileName, String serverLocation, String originLocation) throws JSchException, IOException, NoSuchAlgorithmException {

        //取得remote單檔的md5
        String response = CommonSSHUtils.useSshCommand(connectionConfig, "md5sum " + serverLocation + fileName);
        log.info(response);
        if (StringUtils.isNotBlank(response) && response.contains("No such file or directory")) {
            return FileHashStatusEnums.FILE_NOT_EXIST;
        }
        String serverMd5String = response.split("\t")[0];
        serverMd5String = serverMd5String.split(" ")[0];
        //取得origin檔案的md5
        String filePath = originLocation + fileName;
        //todo 確認檔案是否存在
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        String originMd5String = new BigInteger(1, hash).toString(16);
        log.info("serverMd5String : {}", serverMd5String);
        log.info("originMd5String : {}", originMd5String);
        return originMd5String.equals(serverMd5String) ? FileHashStatusEnums.FILE_HASH_SAME : FileHashStatusEnums.FILE_HASH_DIFF;
    }

    private void handleSingleFileComparisonResult(
            FileHashStatusEnums fileHashStatus,
            IwgHostsDTO iwgHostsDTO,
            ConnectionConfig connectionConfig,
            String originLocation,
            String serverLocation,
            String fileName,
            Instant triggerTime,
            Instant finishTime
    ) throws JSchException, IOException {

        switch (fileHashStatus) {
            case FILE_HASH_SAME:
                log.info("check {} is normal", fileName);
                iwgHostsLogsService.writeCheckNormalLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, serverLocation + fileName);
                break;
            case FILE_HASH_DIFF:
                // 1:寫log 2.替換檔案 3.寄信
                log.warn("{} md5 is different ", fileName);
                iwgHostsLogsService.writeCheckFailLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, serverLocation + fileName, iwgHostsDTO.getSmsReceiver(), iwgHostsDTO.getMailReceiver());
                CommonSSHUtils.useScpCopyLocalFileToRemote(connectionConfig, originLocation, serverLocation, fileName);
                StringBuilder sb = new StringBuilder();
                sb.append("主機:").append(iwgHostsDTO.getHostname()).append(",檔案路徑:").append(serverLocation + fileName).append(",該檔案有異動");
                sb.append("\n並以替換為iwg主機中的正確版本");
                iwgMailSendService.saveAdmMailWithReceiverAndContent(iwgHostsDTO.getMailReceiver(), sb.toString());
                break;
            case FILE_NOT_EXIST:
                // 1:寫log 2.放上檔案 3.寄信
                log.warn("{} is not exist", fileName);
                iwgHostsLogsService.writeCheckFailLog(iwgHostsDTO.getHostname(), iwgHostsDTO.getPort(), triggerTime, finishTime, serverLocation + fileName, iwgHostsDTO.getSmsReceiver(), iwgHostsDTO.getMailReceiver());
                CommonSSHUtils.useScpCopyLocalFileToRemote(connectionConfig, originLocation, serverLocation, fileName);
                log.info("upload file {} finish", fileName);
                StringBuilder sb1 = new StringBuilder();
                sb1.append("主機:").append(iwgHostsDTO.getHostname()).append(",檔案路徑:").append(serverLocation + fileName).append(",該檔案不存在");
                sb1.append("\n並上傳主機中的正確版本");
                iwgMailSendService.saveAdmMailWithReceiverAndContent(iwgHostsDTO.getMailReceiver(), sb1.toString());
                break;
        }


    }


    public Map<String, String> getServerFolderFilesAndMd5Map(ConnectionConfig connectionConfig, String targetFolderName) throws JSchException {

        //取得target folder的所有fileName
        String command = "ls -l " + targetFolderName + " | grep -v '^d' | awk '{print $9}'";
        String result = CommonSSHUtils.useSshCommand(connectionConfig, command);

        Map<String, String> filesInFolderMd5Map = new HashMap<>();
        List<String> fileNameList = Arrays.asList(result.split("\n"));
        //針對每個fileName去取的md5
        for (String singleFile : fileNameList) {
            String singleResult = CommonSSHUtils.useSshCommand(connectionConfig, "md5sum " + targetFolderName + singleFile);
            //result will be like "md5HashId fileName", need split by space
            filesInFolderMd5Map.put(singleFile, singleResult.split(" ")[0]);
        }
        return filesInFolderMd5Map;
    }


}
