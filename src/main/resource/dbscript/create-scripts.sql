

DROP DATABASE IF EXISTS `hrportaldb_DRT`;
CREATE DATABASE  `hrportaldb_DRT`;

create table hrportaldb_DRT.employee
(
	`key` int not null auto_increment
		primary key,
	emp_id varchar(20) not null,
	first_name varchar(30) not null,
	last_name varchar(30) null,
	gender varchar(6) not null,
	date_of_birth datetime null,
	date_of_joining datetime not null,
	maritial_status varchar(9) null,
	pan_card_no varchar(20) null,
	pp_no varchar(20) null,
	pp_expiry_date datetime null,
	pp_issued_date datetime null,
	aadhar_card_no varchar(12) null,
	blood_group varchar(5) null,
	email_id varchar(30) not null,
	created_by varchar(50) not null,
	updated_by varchar(50) not null,
	date_of_relieving datetime null,
	is_billable tinyint(1) default '0' null,
	is_active tinyint(1) default '1' null,
	photo blob null,
	comments tinytext null,
	created_date datetime default CURRENT_TIMESTAMP not null,
	updated_date datetime default CURRENT_TIMESTAMP not null,
	mobile_telephone_number varchar(10) null,
	constraint emp_id_UNIQUE
		unique (emp_id)
)
;

create table hrportaldb_DRT.emp_experience
(
	`key` int not null auto_increment
		primary key,
	company_name varchar(50) not null,
	start_date datetime not null,
	end_date datetime not null,
	skills varchar(100) null,
	total_exp varchar(15) null,
	emp_key int not null,
	created_by varchar(50) not null,
	created_date datetime default CURRENT_TIMESTAMP not null,
	updated_by varchar(50) not null,
	updated_date datetime default CURRENT_TIMESTAMP not null
)
;



create table hrportaldb_DRT.project
(
	`key` int not null auto_increment
		primary key,
	project_name varchar(100) not null,
	module_name varchar(100) not null,
	domain_name varchar(30) not null,
	project_start_date datetime not null,
	project_end_date datetime not null,
	created_by varchar(50) not null,
	updated_by varchar(50) not null,
	client_name varchar(50) null,
	created_date datetime default CURRENT_TIMESTAMP not null,
	updated_date datetime default CURRENT_TIMESTAMP not null
)
;

create table hrportaldb_DRT.emp_project
(
	`key` int not null auto_increment
		primary key,
	project_key int not null,
	emp_key int not null,
	is_active tinyint(1) default '1' not null,
	start_date datetime not null,
	end_date datetime not null,
	created_by varchar(50) not null,
	updated_by varchar(50) not null,
	created_date datetime default CURRENT_TIMESTAMP not null,
	updated_date datetime default CURRENT_TIMESTAMP not null
)
;





CREATE TABLE `hrportaldb_DRT`.`emp_contact_info` (
  `key` BIGINT(11) NOT NULL AUTO_INCREMENT,
`type` VARCHAR(12) NOT NULL,
  `address_line1` VARCHAR(50) NOT NULL,
  `address_line2` VARCHAR(50) NULL,
  `city` VARCHAR(20) NULL,
  `state` VARCHAR(20) NULL,
  `zip_code` INT(8) NULL,
  `phone_num` VARCHAR(20) NOT NULL,
  `land_line_num` VARCHAR(20) NULL,
  `emergency_contact_num` VARCHAR(20) NOT NULL,
  `emergency_contact_name` VARCHAR(50) NOT NULL,
  `emp_key` BIGINT(11) NOT NULL,
  `created_by` BIGINT(11) NOT NULL,
  `created_date` DATETIME NOT NULL DEFAULT NOW(),
  `updated_by` BIGINT(11) NOT NULL,
  `updated_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`key`)) ENGINE = InnoDB;



-- Script for creation of pay scale table--

CREATE TABLE `hrportaldb_DRT`.`employee_pay_scale_info` (
  `key` INT NOT NULL,
  `finacial_year` VARCHAR(45) NULL,
  `ctc` DOUBLE NULL,
  `basic` DOUBLE NULL,
  `hra` DOUBLE NULL,
  `medical_allowances` DOUBLE NULL,
  `special_allowances` DOUBLE NULL,
  `conveyance_allowances` DOUBLE NULL,
  `other_allowances` DOUBLE NULL,
  `monthly_pay` DOUBLE NULL,
  `employee_pf` DOUBLE NULL,
  `employer_pf` DOUBLE NULL,
  `active_status` INT NULL,
  `emp_key` INT NOT NULL,
  `created_by` VARCHAR(45) NULL,
  `updated_by` VARCHAR(45) NULL,
  `created_date` DATETIME NULL,
  `updated_date` DATETIME NULL,
  PRIMARY KEY (`key`)
  );
  
ALTER TABLE `hrportaldb_DRT`.`emp_project` 
CHANGE COLUMN `project_key` `project_id` INT(11) NOT NULL ;

ALTER TABLE `hrportaldb_DRT`.`employee` 
CHANGE COLUMN `email_id` `email_id` VARCHAR(30) NULL ;

ALTER TABLE `hrportaldb_DRT`.`emp_project` 
CHANGE COLUMN `start_date` `empproj_start_date` DATETIME NOT NULL ;

ALTER TABLE `hrportaldb_DRT`.`emp_project` 
CHANGE COLUMN `end_date` `project_end_date` DATETIME NOT NULL ;

ALTER TABLE `hrportaldb_DRT`.`employee` 
CHANGE COLUMN `is_active` `emp_isactive` TINYINT(1) NULL DEFAULT '1' ;

ALTER TABLE `hrportaldb_DRT`.`emp_project` 
CHANGE COLUMN `is_active` `empproj_isactive` TINYINT(1) NOT NULL DEFAULT '1' ;

ALTER TABLE hrportaldb_DRT.emp_project ADD project_name VARCHAR(50) NULL;