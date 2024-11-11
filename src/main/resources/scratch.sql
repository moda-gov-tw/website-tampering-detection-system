-- PWC_DB.dbo.IWG_HOSTS definition

-- Drop table

-- DROP TABLE PWC_DB.dbo.IWG_HOSTS;

CREATE TABLE PWC_DB.dbo.IWG_HOSTS (
	ID bigint IDENTITY(1,1) NOT NULL,
	HOSTNAME nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NOT NULL,
	PORT int NOT NULL,
	USERNAME nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NOT NULL,
	PASSWORD nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NOT NULL,
	ACTIVE varchar(1) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	MAIL_RECEIVER nvarchar(1000) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	SMS_RECEIVER nvarchar(1000) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	UPDATE_USER nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	UPDATE_TIME datetime NULL,
	CREATE_USER nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CREATE_TIME datetime NULL,
	TARGET_FILENAME nvarchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CONSTRAINT PK_IWG_HOSTS PRIMARY KEY (ID)
);



CREATE TABLE PWC_DB.dbo.IWG_HOSTS_LOGS (
	ID bigint IDENTITY(1,1) NOT NULL,
	HOSTNAME nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NOT NULL,
	PORT int NOT NULL,
	TRIGGER_TIME datetime NOT NULL,
	FINISH_TIME datetime NOT NULL,
	[RESULT] nvarchar(5) COLLATE Chinese_Taiwan_Stroke_CI_AI NOT NULL,
	SMS_STATUS nvarchar(1000) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	MAIL_STATUS nvarchar(1000) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CREATE_USER nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CREATE_TIME datetime NULL,
	UPDATE_USER nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	UPDATE_TIME datetime NULL,
	TARGET_FILENAME nvarchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CONSTRAINT PK_IWG_HOSTS_LOGS PRIMARY KEY (ID)
);



CREATE TABLE PWC_DB.dbo.IWG_HOSTS_TARGET (
	ID bigint IDENTITY(1,1) NOT NULL,
	HOSTNAME nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NOT NULL,
	PORT int NOT NULL,
	FILE_NAME varchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	ORIGIN_FILE_LOCATION nvarchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	TARGET_FILE_LOCATION nvarchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	ACTIVE varchar(1) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	ORIGIN_FOLDER nvarchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	TARGET_FOLDER nvarchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CREATE_USER nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CREATE_TIME datetime NULL,
	UPDATE_USER nvarchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	UPDATE_TIME datetime NULL,
	PROFILE varchar(50) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	TARGET_IN_LOCAL_LOCATION varchar(100) COLLATE Chinese_Taiwan_Stroke_CI_AI NULL,
	CONSTRAINT PK__IWG_HOST__3214EC27127DFD08 PRIMARY KEY (ID)
);