## 為何要有這個IWG
要監控多台server主機，確認主機裡面特定資料夾內的檔案是否有異動

## 程式怎麼看
看ScheduledTaskService這一支,所設定的排程主要在做甚麼

## 打包指令
    #在iwg底下,先clean
    mvn clean
    #打包到target底下
    mvn package -DskipTests=true -P {profile}
    
    #profile:
        dev: IDE開發環境用
        prod: 正式環境用

## 機制介紹

![image](https://i.imgur.com/BjQwT6v.png)


## IWG機制的Table設計簡述

![image](https://i.imgur.com/16EcA6i.png)

#
HOSTNAME: 	iwg server會連到哪一台server做資料夾比對
ORIGIN_FOLDER:	iwg主機裡面,本地的ref資料夾
TARGET_FOLDER:	server主機的要被監控資料夾,

## IWG_HOSTS_TARGET欄位說明

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


## 比對不一樣的處理方式
   1. 如果IWG ref有 被監控的server沒有
         
      會把ref裡面的檔案copy過去
   2. 如果IWG ref沒有,被監控的server有
      
      會把監控server的檔案轉移到${TARGET_FOLDER_BK}底下

   3. 如果IWG的檔案和被監控的server的檔案內容不一樣
      
      會把監控server的檔案轉移到${TARGET_FOLDER_BK}底下
      在把ref裡面的檔案copy過去