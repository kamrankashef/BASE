-- DROP DATABASE IF EXISTS application;
-- CREATE DATABASE application;

-- use application;

-- GRANT ALL PRIVILEGES ON application.* TO 'application'@'localhost' IDENTIFIED BY 'password';

-- DROP TABLE player_scouting;

CREATE TABLE player_scouting (
 player_scouting_uuid CHAR(36) PRIMARY KEY
,"3pa" INT
,"3pm" INT
,ast INT
,away_team NVARCHAR(1024)
,birth_date NVARCHAR(1024)
,birth_date_aug DATE NULL DEFAULT NULL
,blk INT
,contribution_bullseye NVARCHAR(1024)
,contribution_high NVARCHAR(1024)
,contribution_low NVARCHAR(1024)
,created_on NVARCHAR(1024) NOT NULL
,created_on_aug DATETIMEOFFSET(3) NULL DEFAULT NULL
,defense NVARCHAR(MAX)
,drb INT
,edited_on NVARCHAR(1024) NOT NULL
,edited_on_aug DATETIMEOFFSET(3) NULL DEFAULT NULL
,eval NVARCHAR(1024) NOT NULL
,fga INT
,fgm INT
,fta INT
,ftm INT
,game_id BIGINT NOT NULL
,height NVARCHAR(1024) NOT NULL
,home_team NVARCHAR(1024)
,intel_background NVARCHAR(128)
,intel_character NVARCHAR(128)
,intel_game_day NVARCHAR(1024)
,intel_injury NVARCHAR(128)
,intel_law NVARCHAR(128)
,intel_level NVARCHAR(1024) NOT NULL
,intel_on_court_evals NVARCHAR(128)
,intel_org NVARCHAR(1024)
,intel_personality NVARCHAR(128)
,intel_relationship NVARCHAR(1024)
,intel_social_media NVARCHAR(128)
,knicks_fit NVARCHAR(MAX)
,league NVARCHAR(1024) NOT NULL
,level NVARCHAR(1024) NOT NULL
,nba_position_role NVARCHAR(1024)
,nba_skill NVARCHAR(1024)
,offense NVARCHAR(MAX)
,orb INT
,pf INT
,player NVARCHAR(1024) NOT NULL
,player_id BIGINT NOT NULL
,pts INT
,report_date NVARCHAR(1024)
,report_date_aug DATE NULL DEFAULT NULL
,report_id BIGINT NOT NULL
,scout NVARCHAR(1024) NOT NULL
,scout_id BIGINT NOT NULL
,seconds_played INT
,setting NVARCHAR(1024) NOT NULL
,stl INT
,team NVARCHAR(1024)
,tov INT
,weight INT
,created_at DATETIMEOFFSET NULL DEFAULT NULL
,modified_at DATETIMEOFFSET 
,deleted_at DATETIMEOFFSET NULL DEFAULT NULL
);
-- Indexes


