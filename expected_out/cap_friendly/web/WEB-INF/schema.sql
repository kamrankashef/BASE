-- DROP DATABASE IF EXISTS nhlcontract;
-- CREATE DATABASE nhlcontract;

-- use nhlcontract;

-- GRANT ALL PRIVILEGES ON nhlcontract.* TO 'nhlcontract'@'localhost' IDENTIFIED BY 'password';

-- DROP TABLE cap_friendly_player;
-- DROP TABLE cap_friendly_contract;
-- DROP TABLE cap_friendly_contract_year;
-- DROP TABLE cap_friendly_stat_by_year;

CREATE TABLE cap_friendly_player (
 cap_friendly_player_uuid CHAR(36) PRIMARY KEY
,age INT
,birth_date_str VARCHAR(1024)
,draft_rount INT
,draft_year INT
,drafted_by VARCHAR(1024)
,drafted_overall INT
,elc_signing_age INT
,elite_prospect_id LONG
,full_name VARCHAR(1024)
,height_str VARCHAR(1024)
,number INT
,shoots VARCHAR(1024)
,team_name VARCHAR(1024)
,waivers_signing_age INT
,created_at TIMESTAMP NULL DEFAULT NULL
,modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
,deleted_at TIMESTAMP NULL DEFAULT NULL
) ENGINE=InnoDB;
-- Indexes


CREATE TABLE cap_friendly_contract (
 cap_friendly_contract_uuid CHAR(36) PRIMARY KEY
,cap_friendly_player_guid CHAR(36) NOT NULL
,FOREIGN KEY (cap_friendly_player_guid) REFERENCES cap_friendly_player(cap_friendly_player_guid) ON DELETE CASCADE
,ch DOUBLE
,expiry_status VARCHAR(1024)
,length_str VARCHAR(1024)
,signing_date TIMESTAMP NULL DEFAULT NULL
,signing_team VARCHAR(1024)
,type VARCHAR(1024)
,value DOUBLE
,created_at TIMESTAMP NULL DEFAULT NULL
,modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
,deleted_at TIMESTAMP NULL DEFAULT NULL
) ENGINE=InnoDB;
-- Indexes


CREATE TABLE cap_friendly_contract_year (
 cap_friendly_contract_year_uuid CHAR(36) PRIMARY KEY
,cap_friendly_contract_guid CHAR(36) NOT NULL
,FOREIGN KEY (cap_friendly_contract_guid) REFERENCES cap_friendly_contract(cap_friendly_contract_guid) ON DELETE CASCADE
,ahl_salary DOUBLE
,avv DOUBLE
,cap_hit DOUBLE
,clause VARCHAR(1024)
,nhl_salary DOUBLE
,p_bonus DOUBLE
,s_bonus DOUBLE
,season VARCHAR(1024)
,created_at TIMESTAMP NULL DEFAULT NULL
,modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
,deleted_at TIMESTAMP NULL DEFAULT NULL
) ENGINE=InnoDB;
-- Indexes


CREATE TABLE cap_friendly_stat_by_year (
 cap_friendly_stat_by_year_uuid CHAR(36) PRIMARY KEY
,cap_friendly_player_guid CHAR(36) NOT NULL
,FOREIGN KEY (cap_friendly_player_guid) REFERENCES cap_friendly_player(cap_friendly_player_guid) ON DELETE CASCADE
,a DOUBLE
,g DOUBLE
,gp DOUBLE
,league VARCHAR(1024)
,pim DOUBLE
,playoffs VARCHAR(1024)
,playoffs_a DOUBLE
,playoffs_g DOUBLE
,playoffs_gp DOUBLE
,playoffs_pim DOUBLE
,playoffs_tp DOUBLE
,season VARCHAR(1024)
,team VARCHAR(1024)
,tp DOUBLE
,created_at TIMESTAMP NULL DEFAULT NULL
,modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
,deleted_at TIMESTAMP NULL DEFAULT NULL
) ENGINE=InnoDB;
-- Indexes


