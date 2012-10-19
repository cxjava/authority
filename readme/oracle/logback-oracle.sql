-- Logback: the reliable, generic, fast and flexible logging framework.
-- Copyright (C) 1999-2010, QOS.ch. All rights reserved.
--
-- See http://logback.qos.ch/license.html for the applicable licensing 
-- conditions.


-- This SQL script creates the required tables by ch.qos.logback.classic.db.DBAppender
--
-- It is intended for Oracle 9i, 10g and 11g databases. Tested on version 9.2, 
-- 10g and 11g.

-- The following lines are useful in cleaning any previously existing tables 

drop TRIGGER  logging_event_id_seq_trig; 
drop SEQUENCE  logging_event_id_seq; 
drop table  logging_event_property; 
drop table  logging_event_exception; 
drop table  logging_event; 
COMMIT; 

CREATE SEQUENCE logging_event_id_seq MINVALUE 1 START WITH 1;

CREATE TABLE logging_event 
  (
    timestmp         VARCHAR(24) NOT NULL,
    formatted_message  VARCHAR2(4000) NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    arg0              VARCHAR(254),
    arg1              VARCHAR(254),
    arg2              VARCHAR(254),
    arg3              VARCHAR(254),
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4) NOT NULL,
    event_id          NUMBER(10) PRIMARY KEY
  );


comment on column logging_event.timestmp is
'创建记录事件的时间戳';
comment on column logging_event.formatted_message is
'经 org.slf4j.impl.MessageFormatter  格式化后，被添加到记录事件的消息。';
comment on column logging_event.logger_name is
'执行记录请求的 logger。';
comment on column logging_event.level_string is
'记录事件的级别。';
comment on column logging_event.thread_name is
'进程名称';
comment on column logging_event.reference_flag is
'用于标识那些关联了异常或 MDCproperty  值的记录事件。 其值由 ch.qos.logback.classic.db.DBHelper 计算。包含 MDC或上下文属性的记录事件的 reference_flag 是 1。包含异常的reference_flag 是 2。两者都包含的 reference_flag 是 3。';
comment on column logging_event.arg0 is
'参数1';
comment on column logging_event.arg1 is
'参数2';
comment on column logging_event.arg2 is
'参数3';
comment on column logging_event.arg3 is
'参数4';
comment on column logging_event.caller_filename is
'执行记录请求的文件名。 ';
comment on column logging_event.caller_class is
'执行记录请求的类。';
comment on column logging_event.caller_method is
'执行记录请求的方法。';
comment on column logging_event.caller_line is
'执行记录请求的行号。';
comment on column logging_event.event_id is
'记录事件的数据库 ID。 ';
-- the / suffix may or may not be needed depending on your SQL Client
-- Some SQL Clients, e.g. SQuirrel SQL has trouble with the following
-- trigger creation command, while SQLPlus (the basic SQL Client which
-- ships with Oracle) has no trouble at all.

CREATE TRIGGER logging_event_id_seq_trig
  BEFORE INSERT ON logging_event
  FOR EACH ROW  
  BEGIN  
    SELECT logging_event_id_seq.NEXTVAL 
    INTO   :NEW.event_id 
    FROM   DUAL;  
  END;
/


CREATE TABLE logging_event_property
  (
    event_id	      NUMBER(10) NOT NULL,
    mapped_key        VARCHAR2(254) NOT NULL,
    mapped_value      VARCHAR2(1024),
    PRIMARY KEY(event_id, mapped_key),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );
  
CREATE TABLE logging_event_exception
  (
    event_id         NUMBER(10) NOT NULL,
    i                SMALLINT NOT NULL,
    trace_line       VARCHAR2(254) NOT NULL,
    PRIMARY KEY(event_id, i),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );
  comment on TABLE logging_event_exception is
'记录系统错误信息';



