-- DROP DATABASE IF EXISTS application;
-- CREATE DATABASE application;

-- use application;

-- GRANT ALL PRIVILEGES ON application.* TO 'application'@'localhost' IDENTIFIED BY 'password';

-- DROP TABLE event;

CREATE TABLE event (
 event_uuid CHAR(36) PRIMARY KEY
,custom_attribute NVARCHAR(128)
,event_eastern_time_zone_time NVARCHAR(1024)
,event_id NVARCHAR(1024)
,event_local_date INT
,event_local_time NVARCHAR(1024)
,event_name NVARCHAR(1024)
,event_number INT
,meeting_description NVARCHAR(1024)
,meeting_employee_id BIGINT
,shift_end_summary NVARCHAR(1024)
,surgery_employee_id BIGINT
,surgery_floor INT
,surgery_group_id BIGINT
,surgery_group_role NVARCHAR(1024)
,surgery_room_number NVARCHAR(1024)
,created_at DATETIMEOFFSET NULL DEFAULT NULL
,modified_at DATETIMEOFFSET NULL DEFAULT NULL
,deleted_at DATETIMEOFFSET NULL DEFAULT NULL
);
-- Indexes


