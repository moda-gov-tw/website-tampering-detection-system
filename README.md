# 網站防竄改偵測系統

## 公共程式描述

當特定網站或系統的檔案目錄需要時時監控以確保未經授權進行竄改時，本系統提供檔案目錄偵測及還原功能，解決系統中檔案遭受入侵竄改時難以及時發現與恢復的問題。

本系統能即時偵測到目標系統中目錄中的變更狀態，並在發現檔案被竄改時，立即通知相關人員，同時自動還原目錄檔案的原始內容，達到保障系統安全維運與資料完整性的目的。

## 內含功能

1. 偵測紀錄查詢
2. 偵測網站設定
3. 通知設定
4. 受竄改還原設定

## 使用技術

1. Spring Boot
2. Spring Data JPA
3. Hibernate
4. H2 Database Engine

## 使用之弱點掃描工具

Fortify SCA

## 授權方式

Apache License 2.0

## 使用案例

行政院公共工程委員會

「公共工程雲端系統資訊服務案」之公共工程雲端系統

## 安裝指南

### 1. 打包指令
   
    - 在iwg底下，先clean： mvn clean
   
    - 打包到target底下： mvn package -DskipTests=true -P {profile}
    
    - profile：
        - dev: IDE開發環境用
        - prod: 正式環境用
   
### 2. 機制介紹

![image](https://i.imgur.com/BjQwT6v.png)

### 3. IWG 機制的 Table 設計簡述

![image](https://i.imgur.com/16EcA6i.png)

#
HOSTNAME: 	iwg server會連到哪一台server做資料夾比對
ORIGIN_FOLDER:	iwg主機裡面,本地的ref資料夾
TARGET_FOLDER:	server主機的要被監控資料夾,

### 4. IWG_HOSTS_TARGET 欄位說明

ps. IWG可以監控單一個檔案 或是監控整個資料夾

如果要監控檔案,要填FILE_NAME,ORIGIN_FILE_LOCATION,TARGET_FILE_LOCATION

如果要監控資料夾,要填ORIGIN_FOLDER,TARGET_FOLDER

|column Name |說明|
|-----|--------|
|ID|PK|
|HOSTNAME|要監控的主機IP|
|PORT|要監控的主機PORT|
|FILE_NAME|要被監控的檔案|
|ORIGIN_FILE_LOCATION|IWG主機的REF檔案資料夾位置(不含檔名)|
|TARGET_FILE_LOCATION|被監控的主機的檔案資料夾位置(不含檔名)|
|ACTIVE|Y/N: 該筆監控是否要啟動|
|ORIGIN_FOLDER|IWG主機的REF檔案資料夾|
|TARGET_FOLDER|被監控的主機的檔案資料夾|
|CREATE_USER||
|CREATE_TIME||
|UPDATE_USER||
|UPDATE_TIME||
|PROFILE |當初開發時測試用..現在沒在用|
|TARGET_IN_LOCAL_LOCATION|當初開發時測試用..現在沒在用|


### 5. 比對不一樣的處理方式
   5-1. 如果 IWG ref 有；被監控的 server 沒有         
      >>>  會把 ref 裡面的檔案 copy 過去
      
   5-2. 如果 IWG ref 沒有；被監控的 server 有      
      >>>  會把監控 server 的檔案轉移到 ${TARGET_FOLDER_BK} 底下

   5-3. 如果 IWG 的檔案和被監控的 server 的檔案內容不一樣      
      >>>  會把監控server的檔案轉移到 ${TARGET_FOLDER_BK} 底下，再把 ref 裡面的檔案 copy 過去
