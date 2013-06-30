------------------------------------------
--Host     : localhost
--Database : openempi


SET SESSION AUTHORIZATION 'openempi';
SET search_path = public, pg_catalog;

-- Structure for table app_user (OID = 34479):
CREATE TABLE app_user (
    id bigint NOT NULL,
    username varchar(50) NOT NULL,
    email varchar(255) NOT NULL,
    phone_number varchar(255),
    password_hint varchar(255),
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    website varchar(255),
    account_expired boolean NOT NULL,
    account_locked boolean NOT NULL,
    credentials_expired boolean NOT NULL,
    city varchar(50) NOT NULL,
    province varchar(100),
    postal_code varchar(15) NOT NULL,
    address varchar(150),
    country varchar(100),
    account_enabled boolean,
    "version" integer,
    "password" varchar(255) NOT NULL
) WITHOUT OIDS;

INSERT INTO app_user (id, username, email, phone_number, password_hint, first_name, last_name, website, account_expired, account_locked, credentials_expired, city, province, postal_code, address, country, account_enabled, "version", "password") VALUES (2, 'user', 'support@sysnetint.com', '', 'A male kitty.', 'Open', 'Empi', 'http://www.sysnetint.com', false, false, false, 'Reston', 'VA', '20191', '', 'US', true, 1, '12dea96fec20593566ab75692c9949596833adc9');
INSERT INTO app_user (id, username, email, phone_number, password_hint, first_name, last_name, website, account_expired, account_locked, credentials_expired, city, province, postal_code, address, country, account_enabled, "version", "password") VALUES (1, 'admin', 'odysseas@sysnetint.com', '', 'A male kitty.', 'Admin', 'User', 'http://www.sysnetint.com', false, false, false, 'Herndon', 'VA', '20171', '', 'US', true, 1, 'd033e22ae348aeb5660fc2140aec35850c4da997');
COMMIT;

-- Structure for table role (OID = 34546):
CREATE TABLE role (
    id bigint NOT NULL,
    name varchar(20),
    description varchar(64)
) WITHOUT OIDS;

INSERT INTO "role" (id, "name", description) VALUES
  (-1, 'ROLE_ADMIN', 'Administrator role (can edit Users)'),
  (-2, 'ROLE_USER', 'Default role for all users');

-- Structure for table user_role (OID = 34551):
CREATE TABLE user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
) WITHOUT OIDS;

INSERT INTO user_role (user_id, role_id) VALUES
 (1, -1),
 (1, -2),
 (2, -2),
 (2, -1);

-- Structure for table person (OID = 34518):
CREATE TABLE person (
    person_id BIGINT NOT NULL,
    original_id varchar(64),

    date_created timestamp without time zone NOT NULL,
    creator_id bigint NOT NULL
) WITHOUT OIDS;

-- Structure for table person_link (OID = 34531):
CREATE TABLE person_link (
    person_link_id integer NOT NULL,
    left_person_id BIGINT NOT NULL,
    right_person_id BIGINT NOT NULL,
    binary_vector varchar(65535),
    continous_vector varchar(65535),
    weight double precision NOT NULL,
    link_state integer NOT NULL,
) WITHOUT OIDS;

-- Structure for table person_match:
CREATE TABLE person_match (
    person_match_id integer NOT NULL,
    match_title varchar(255) NOT NULL,
    left_dataset_id integer NOT NULL,
    right_dataset_id integer NOT NULL,
    matchconfiguration bytea NOT NULL,
    matchfellegisunter bytea,
    blockfellegisunter bytea,
    total_records bigint NOT NULL,
    bf_k_parameter integer,
    bf_fill_factor double precision,
    creator_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL
) WITHOUT OIDS;

-- Structure for table person_match_request:
CREATE TABLE person_match_request (
    person_match_request_id integer NOT NULL,
    dataset_id integer NOT NULL,
    match_name varchar(255) NOT NULL,
    blocking_service_name varchar(255) NOT NULL,
    matching_service_name varchar(255) NOT NULL,
    nonce integer,
    match_pair_stat_half_table_name varchar(255),
    person_match_id integer,
    completed boolean NOT NULL,
    creator_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL
) WITHOUT OIDS;

-- Structure for table field_type:
CREATE TABLE field_type (
    field_type_cd integer NOT NULL,
    field_type_name varchar(64) NOT NULL,
    field_type_description varchar(255),
    field_type_code varchar(64) NOT NULL
) WITHOUT OIDS;

insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (1,'String','String','STR');
insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (2,'Integer','Integer','INT');
insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (3,'BigInt','Big Integer','BIG');
insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (4,'Float','Floating Point','FLT');
insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (5,'Double','Double Precision Floating Point','DBL');
insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (6,'Date','Date (format indicated in field_type_modifier)','DAT');
insert into field_type (field_type_cd, field_type_name, field_type_description, field_type_code) values (7,'Blob','Blob, possible bloom filter encoded data','BLB');

-- Structure for table field_meaning:
CREATE TABLE field_meaning (
    field_meaning_cd integer NOT NULL,
    field_meaning_name varchar(64) NOT NULL,
    field_meaning_description varchar(255),
    field_meaning_code varchar(64) NOT NULL
) WITHOUT OIDS;

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (1,'OriginalId','Original Identifier field','OID');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (2,'GivenName','Given Name, First Name','GNM');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (3,'FamilyName','Family Name, Last Name','FNM');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (4,'MiddleName','Middle Name','MNM');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (5,'NamePrefix','Name Prefix','NPR');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (6,'NameSuffix','Name Suffix','NSU');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (7,'DateOfBirth','Date Of Birth, Birth Date','DOB');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (8,'BirthWeight','Birth Weight','BWT');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (9,'BirthCity','Birth City','BCT');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (10,'BirthState','Birth State','BST');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (11,'BirthCountry','Birth Country','BCN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (12,'MothersMaidenName','Mothers Maiden Name','MMN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (13,'MothersWeightAtBirth','Mothers Weight At Birth','MWB');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (14,'SSN','Social Security Number','SSN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (15,'Gender','Gender, Sex','SEX');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (16,'EthnicGroup','Ethnic Group','EGP');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (17,'Race','Race','RAC');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (18,'Nationality','Nationality','NAT');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (19,'Language','Language','LAN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (20,'Religion','Religion','REL');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (21,'MaritalStatus','Marital Status','MST');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (22,'Degree','Degree','DEG');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (23,'Email','E-mail Address','EML');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (24,'AddressLine1','Address Line 1','AD1');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (25,'AddressLine2','Address Line 2','AD2');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (26,'City','City','CTY');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (27,'County','County','CNY');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (28,'State','State','STA');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (29,'Country','Country','CRY');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (30,'PostalCode','Postal Code, Zip Code','ZIP');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (31,'AddressNumber','Number part of Address','ANM');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (32,'AddressFraction','Fraction part of Address','AFR');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (33,'AddressDirection','Direction part of Address','ADI');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (34,'AddressStreetName','Street Name part of Address','ASN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (35,'AddressType','Type of Address','ATP');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (36,'AddressPostDirection','Post Dircetion part of Address','APD');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (37,'AddressOther','Other feature of Address','AOT');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (38,'Address','Whole address in one line','ADR');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (39,'PhoneCountryCode','Country Code of Phone Number','PCC');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (40,'PhoneAreaCode','Area Code of Phone Number','PAC');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (41,'PhoneNumber','Phone Number','PNM');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (42,'PhoneExtension','Extension of Phone Number','PEX');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (43,'DateCreated','Creation Date','DCR');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (44,'CreatorId','User Id of the Creator User','CRI');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (45,'DateChanged','Change Date','DCN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (46,'ChangedById','User Id of the User who performed changes','CBI');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (47,'DateVoided','Date of record Voidation','DVD');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (48,'VoidedById','User Id of the User who performed the voidation','VBI');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (49,'DiagnosisCodes','List of Diagnosis Codes','DCD');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (50,'DeathIndication','Indication of Death','DIN');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (51,'DeathTime','Time of Death','DTM');

insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (52,'Custom1','Custom Meaning','CM1');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (53,'Custom2','Custom Meaning','CM2');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (54,'Custom3','Custom Meaning','CM3');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (55,'Custom4','Custom Meaning','CM4');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (56,'Custom5','Custom Meaning','CM5');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (57,'Custom6','Custom Meaning','CM6');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (58,'Custom7','Custom Meaning','CM7');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (59,'Custom8','Custom Meaning','CM8');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (60,'Custom9','Custom Meaning','CM9');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (61,'Custom10','Custom Meaning','CM10');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (62,'Custom11','Custom Meaning','CM11');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (63,'Custom12','Custom Meaning','CM12');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (64,'Custom13','Custom Meaning','CM13');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (65,'Custom14','Custom Meaning','CM14');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (66,'Custom15','Custom Meaning','CM15');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (67,'Custom16','Custom Meaning','CM16');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (68,'Custom17','Custom Meaning','CM17');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (69,'Custom18','Custom Meaning','CM18');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (70,'Custom19','Custom Meaning','CM19');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (71,'Custom20','Custom Meaning','CM20');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (72,'Custom21','Custom Meaning','CM21');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (73,'Custom22','Custom Meaning','CM22');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (74,'Custom23','Custom Meaning','CM23');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (75,'Custom24','Custom Meaning','CM24');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (76,'Custom25','Custom Meaning','CM25');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (77,'Custom26','Custom Meaning','CM26');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (78,'Custom27','Custom Meaning','CM27');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (79,'Custom28','Custom Meaning','CM28');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (80,'Custom29','Custom Meaning','CM29');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (81,'Custom30','Custom Meaning','CM30');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (82,'Custom31','Custom Meaning','CM31');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (83,'Custom32','Custom Meaning','CM32');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (84,'Custom33','Custom Meaning','CM33');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (85,'Custom34','Custom Meaning','CM34');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (86,'Custom35','Custom Meaning','CM35');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (87,'Custom36','Custom Meaning','CM36');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (88,'Custom37','Custom Meaning','CM37');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (89,'Custom38','Custom Meaning','CM38');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (90,'Custom39','Custom Meaning','CM39');
insert into field_meaning (field_meaning_cd, field_meaning_name, field_meaning_description, field_meaning_code) values (91,'CBF','Composite Bloom Filter','CBF');

-- Structure for table column_match_information:
CREATE TABLE column_match_information (
    column_match_information_id integer NOT NULL,
    person_match_id integer NOT NULL,
    left_field_name varchar(255) NOT NULL,
    right_field_name varchar(255) NOT NULL,
    field_type_cd integer NOT NULL,
    field_type_modifier varchar(255),
    field_meaning_cd integer NOT NULL,
    comparison_function_name varchar(255),
    fs_m_value double precision,
    fs_u_value double precision,
    bf_proposed_m double precision,
    bf_possible_m double precision,
    bf_final_m double precision
) WITHOUT OIDS;

-- Structure for table dataset:
CREATE TABLE dataset (
  dataset_id     integer NOT NULL,
  user_id        integer NOT NULL,
  table_name     varchar(255) NOT NULL,
  file_name      varchar(255) NOT NULL,
  total_records  bigint NOT NULL,
  imported_ind   char NOT NULL DEFAULT 'N'::bpchar,
  date_created   timestamp WITHOUT TIME ZONE NOT NULL
) WITHOUT OIDS;

-- Structure for table column_information:
CREATE TABLE column_information (
    column_information_id integer NOT NULL,
    dataset_id integer NOT NULL,
    field_name varchar(255) NOT NULL,
    field_type_cd integer NOT NULL,
    field_type_modifier varchar(255),
    field_meaning_cd integer NOT NULL,
    field_transformation varchar(255),
    bf_m_parameter integer,
    bf_k_parameter integer,
    average_field_length double precision,
    number_of_missing integer
) WITHOUT OIDS;

-- Structure for table key:
CREATE TABLE key (
    id BIGINT NOT NULL,
    publickeypart1 bytea,
    publickeypart2 bytea,
    publickeypart3 bytea,
    privatekeypart1 bytea,
    privatekeypart2 bytea,
    privatekeypart3 bytea,
    date_created timestamp without time zone NOT NULL,
    creator_id bigint NOT NULL,
    date_voided timestamp without time zone,
    voided_by_id bigint
) WITHOUT OIDS;

-- Structure for table salt:
CREATE TABLE salt (
    id BIGINT NOT NULL,
    salt bytea,
    date_created timestamp without time zone NOT NULL,
    creator_id bigint NOT NULL,
    date_voided timestamp without time zone,
    voided_by_id bigint
) WITHOUT OIDS;

-- Structure for table user_session (OID = 34556):
CREATE TABLE user_session (
    session_id integer NOT NULL,
    date_created timestamp without time zone NOT NULL,
    session_key varchar(255),
    user_id bigint NOT NULL
) WITHOUT OIDS;

-- Structure for table audit_event (OID = 59451):
CREATE TABLE audit_event (
    audit_event_id integer NOT NULL,
    date_created timestamp without time zone NOT NULL,
    audit_event_type_cd integer NOT NULL,
    audit_event_description varchar(255),
    ref_person_id BIGINT,
    alt_ref_person_id BIGINT,
    ref_key_id BIGINT,
    ref_salt_id BIGINT,
    creator_id integer NOT NULL
) WITHOUT OIDS;

-- Structure for table audit_event_type (OID = 59461):
CREATE TABLE audit_event_type (
    audit_event_type_cd integer NOT NULL,
    audit_event_type_name varchar(64) NOT NULL,
    audit_event_type_description varchar(255),
    audit_event_type_code varchar(64) NOT NULL
) WITHOUT OIDS;

INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (1, 'Add Person', 'Add new person record', 'ADD');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (2, 'Delete Person', 'Delete a person record', 'DEL');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (3, 'Import Person', 'Import a person record', 'IMP');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (4, 'Merge Person', 'Merge a person record', 'MRG');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (5, 'Update Person', 'Update a person record', 'UPD');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (6, 'Unmerge Person', 'Unmerge a person record from another one', 'UMG');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (7, 'Add Key', 'Add new key record', 'ADDKEY');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (8, 'Delete Key', 'Delete a key record', 'DELKEY');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (9, 'Add Salt', 'Add new salt record', 'ADDSALT');
INSERT INTO audit_event_type (audit_event_type_cd, audit_event_type_name, audit_event_type_description, audit_event_type_code) VALUES (10, 'Delete Salt', 'Delete a salt record', 'DELSALT');

-- Structure for table person_link_review (OID = 59489):
CREATE TABLE person_link_review (
    person_link_review_id integer NOT NULL,
    left_person_id BIGINT NOT NULL,
    right_person_id BIGINT NOT NULL,
    date_created timestamp without time zone NOT NULL,
    weight double precision,
    creator_id bigint NOT NULL,
    reviewer_id integer,
    date_reviewed integer
) WITHOUT OIDS;

-- Structure for table match_pair_stat_half:
CREATE TABLE match_pair_stat_half (
    match_pair_stat_half_id BIGINT NOT NULL,
    person_pseudo_id BIGINT NOT NULL,
    match_state boolean NOT NULL
) WITHOUT OIDS;

-- Structure for table match_pair_stat:
CREATE TABLE match_pair_stat (
    match_pair_stat_id BIGINT NOT NULL,
    left_person_pseudo_id BIGINT NOT NULL,
    right_person_pseudo_id BIGINT NOT NULL,
    match_state boolean NOT NULL
) WITHOUT OIDS;


-- Definition for sequence audit_event_seq
CREATE SEQUENCE audit_event_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
  
-- Definition for sequence hibernate_sequence (OID = 34671):
CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence person_link_seq (OID = 34677):
CREATE SEQUENCE person_link_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence person_match_seq:
CREATE SEQUENCE person_match_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence person_match_request_seq:
CREATE SEQUENCE person_match_request_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence column_match_information_seq:
CREATE SEQUENCE column_match_information_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence person_link_review_seq:
CREATE SEQUENCE person_link_review_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence person_seq (OID = 34679):
CREATE SEQUENCE person_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence dataset_seq:
CREATE SEQUENCE dataset_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence column_information_seq:
CREATE SEQUENCE column_information_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence key_seq:
CREATE SEQUENCE key_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence salt_seq:
CREATE SEQUENCE salt_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence user_session_seq (OID = 34681):
CREATE SEQUENCE user_session_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence match_pair_stat_half_seq:
CREATE SEQUENCE match_pair_stat_half_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Definition for sequence match_pair_stat_seq:
CREATE SEQUENCE match_pair_stat_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


-- Definition for index idx_audit_event_ref_person (OID = 59456):
CREATE INDEX idx_audit_event_ref_person ON audit_event USING btree (ref_person_id);

-- Definition for index idx_audit_event_type_code (OID = 59468):
CREATE INDEX idx_audit_event_type_code ON audit_event_type USING btree (audit_event_type_code);

-- Definition for index column_information_dataset_id:
CREATE INDEX column_information_dataset_id ON column_information USING btree (dataset_id);

-- Definition for index person_link_right_person_id (OID = 34712):
CREATE INDEX person_link_right_person_id ON person_link USING btree (right_person_id);

-- Definition for index person_link_left_person_id (OID = 34713):
CREATE INDEX person_link_left_person_id ON person_link USING btree (left_person_id);

-- Definition for index person_link_weight:
CREATE INDEX person_link_weight ON person_link USING btree (weight);

-- Definition for index person_match_left_dataset_id:
CREATE INDEX person_match_left_dataset_id ON person_match USING btree (left_dataset_id);

-- Definition for index person_match_right_dataset_id:
CREATE INDEX person_match_right_dataset_id ON person_match USING btree (right_dataset_id);

-- Definition for index person_match_request_dataset_id:
CREATE INDEX person_match_request_dataset_id ON person_match_request USING btree (dataset_id);

-- Definition for index column_match_information_person_match_id:
CREATE INDEX column_match_information_person_match_id ON column_match_information USING btree (person_match_id);

-- Definition for index match_pair_stat_half_id:
CREATE INDEX match_pair_stat_half_id ON match_pair_stat_half USING btree (match_pair_stat_half_id);

-- Definition for index match_pair_stat_half_person_pseudo_id:
CREATE INDEX match_pair_stat_half_person_pseudo_id ON match_pair_stat_half USING btree (person_pseudo_id);

-- Definition for index match_pair_stat_id:
CREATE INDEX match_pair_stat_id ON match_pair_stat USING btree (match_pair_stat_id);

-- Definition for index idx_audit_event_ref_key:
CREATE INDEX idx_audit_event_ref_key ON audit_event USING btree (ref_key_id);

-- Definition for index idx_audit_event_ref_salt:
CREATE INDEX idx_audit_event_ref_salt ON audit_event USING btree (ref_salt_id);

-- Definition for index role_name (OID = 34729):
CREATE UNIQUE INDEX role_name ON role USING btree (name);

-- Definition for index user_session_session_key (OID = 34740):
CREATE UNIQUE INDEX user_session_session_key ON user_session USING btree (session_key);

-- Definition for index user_session_date_created (OID = 34741):
CREATE INDEX user_session_date_created ON user_session USING btree (date_created);

-- Definition for index app_user_pkey (OID = 34482):
ALTER TABLE ONLY app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);

-- Definition for index app_user_username_key (OID = 34484):
ALTER TABLE ONLY app_user
    ADD CONSTRAINT app_user_username_key UNIQUE (username);

-- Definition for index app_user_email_key (OID = 34486):
ALTER TABLE ONLY app_user
    ADD CONSTRAINT app_user_email_key UNIQUE (email);

-- Definition for index person_pkey (OID = 34524):
ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);

-- Definition for index person_link_pkey (OID = 34534):
ALTER TABLE ONLY person_link
    ADD CONSTRAINT person_link_pkey PRIMARY KEY (person_link_id);

-- Definition for index person_match_pkey:
ALTER TABLE ONLY person_match
    ADD CONSTRAINT person_match_pkey PRIMARY KEY (person_match_id);

-- Definition for index person_match_request_pkey:
ALTER TABLE ONLY person_match_request
    ADD CONSTRAINT person_match_request_pkey PRIMARY KEY (person_match_request_id);

-- Definition for index field_type_pkey:
ALTER TABLE ONLY field_type
    ADD CONSTRAINT field_type_pkey PRIMARY KEY (field_type_cd);

-- Definition for index field_meaning_pkey:
ALTER TABLE ONLY field_meaning
    ADD CONSTRAINT field_meaning_pkey PRIMARY KEY (field_meaning_cd);

-- Definition for index column_information_pkey:
ALTER TABLE ONLY column_information
    ADD CONSTRAINT column_information_pkey PRIMARY KEY (column_information_id);

-- Definition for index column_match_information_pkey:
ALTER TABLE ONLY column_match_information
    ADD CONSTRAINT column_match_information_pkey PRIMARY KEY (column_match_information_id);

-- Definition for index dataset_pkey:
ALTER TABLE ONLY dataset
    ADD CONSTRAINT dataset_pkey PRIMARY KEY (dataset_id);

-- Definition for index match_pair_stat_half_pkey:
ALTER TABLE ONLY match_pair_stat_half
    ADD CONSTRAINT match_pair_stat_half_pkey PRIMARY KEY (match_pair_stat_half_id);

-- Definition for index match_pair_stat_pkey:
ALTER TABLE ONLY match_pair_stat
    ADD CONSTRAINT match_pair_stat_pkey PRIMARY KEY (match_pair_stat_id);

-- Definition for index key_pkey:
ALTER TABLE ONLY key
    ADD CONSTRAINT key_pkey PRIMARY KEY (id);

-- Definition for index salt_pkey:
ALTER TABLE ONLY salt
    ADD CONSTRAINT salt_pkey PRIMARY KEY (id);

-- Definition for index role_pkey (OID = 34549):
ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);

-- Definition for index user_role_pkey (OID = 34554):
ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);

-- Definition for index user_session_pkey (OID = 34559):
ALTER TABLE ONLY user_session
    ADD CONSTRAINT user_session_pkey PRIMARY KEY (session_id);


-- Definition for index fk_left_person_id (OID = 34714):
ALTER TABLE ONLY person_link
    ADD CONSTRAINT fk_left_person_id FOREIGN KEY (left_person_id) REFERENCES person(person_id);

-- Definition for index fk_right_person_id (OID = 34719):
ALTER TABLE ONLY person_link
    ADD CONSTRAINT fk_right_person_id FOREIGN KEY (right_person_id) REFERENCES person(person_id);

-- Definition for index fk_left_dataset_id:
ALTER TABLE ONLY person_match
    ADD CONSTRAINT fk_left_dataset_id FOREIGN KEY (left_dataset_id) REFERENCES dataset(dataset_id);

-- Definition for index fk_right_dataset_id:
ALTER TABLE ONLY person_match
    ADD CONSTRAINT fk_right_dataset_id FOREIGN KEY (right_dataset_id) REFERENCES dataset(dataset_id);

-- Definition for index fk_created_by_app_user_match:
ALTER TABLE ONLY person_match
    ADD CONSTRAINT fk_created_by_app_user_match FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for unique constraint person_match_match_title:
ALTER TABLE ONLY person_match
    ADD CONSTRAINT un_person_match_match_title UNIQUE (match_title);

-- Definition for index fk_person_match_request_dataset_id:
ALTER TABLE ONLY person_match_request
    ADD CONSTRAINT fk_person_match_request_dataset_id FOREIGN KEY (dataset_id) REFERENCES dataset(dataset_id);

-- Definition for index fk_person_match_request_person_match_id:
ALTER TABLE ONLY person_match_request
    ADD CONSTRAINT fk_person_match_request_person_match_id FOREIGN KEY (person_match_id) REFERENCES person_match(person_match_id);

-- Definition for index fk_created_by_app_user_match_request:
ALTER TABLE ONLY person_match_request
    ADD CONSTRAINT fk_created_by_app_user_match_request FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for index fk_column_match_information_person_match_id:
ALTER TABLE ONLY column_match_information
    ADD CONSTRAINT fk_column_match_information_person_match_id FOREIGN KEY (person_match_id) REFERENCES person_match(person_match_id);

-- Definition for index fk_column_match_information_field_type_cd:
ALTER TABLE ONLY column_match_information
    ADD CONSTRAINT fk_column_match_information_field_type_cd FOREIGN KEY (field_type_cd) REFERENCES field_type(field_type_cd);

-- Definition for index fk_column_match_information_field_meaning_cd:
ALTER TABLE ONLY column_match_information
    ADD CONSTRAINT fk_column_match_information_field_meaning_cd FOREIGN KEY (field_meaning_cd) REFERENCES field_meaning(field_meaning_cd);

-- Definition for index fk_user_role_user (OID = 34730):
ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES app_user(id);

-- Definition for index fk_user_role_role (OID = 34735):
ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id);

-- Definition for index fk_user_session_user (OID = 34742):
ALTER TABLE ONLY user_session
    ADD CONSTRAINT fk_user_session_user FOREIGN KEY (user_id) REFERENCES app_user(id);

-- Definition for index fk_created_by_app_user (OID = 34792):
ALTER TABLE ONLY person
    ADD CONSTRAINT fk_created_by_app_user FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for index fk_key_created_by_app_user:
ALTER TABLE ONLY key
    ADD CONSTRAINT fk_key_created_by_app_user FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for index fk_key_voided_by_app_user:
ALTER TABLE ONLY key
    ADD CONSTRAINT fk_key_voided_by_app_user FOREIGN KEY (voided_by_id) REFERENCES app_user(id);

-- Definition for index fk_salt_created_by_app_user:
ALTER TABLE ONLY salt
    ADD CONSTRAINT fk_salt_created_by_app_user FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for index fk_salt_voided_by_app_user:
ALTER TABLE ONLY salt
    ADD CONSTRAINT fk_salt_voided_by_app_user FOREIGN KEY (voided_by_id) REFERENCES app_user(id);


-- Definition for index audit_event_pkey (OID = 59454):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT audit_event_pkey PRIMARY KEY (audit_event_id);

-- Definition for index audit_event_type_pkey (OID = 59464):
ALTER TABLE ONLY audit_event_type
    ADD CONSTRAINT audit_event_type_pkey PRIMARY KEY (audit_event_type_cd);

-- Definition for index idx_audit_event_type_name (OID = 59466):
ALTER TABLE ONLY audit_event_type
    ADD CONSTRAINT idx_audit_event_type_name UNIQUE (audit_event_type_name);

-- Definition for index fk_audit_event_type_cd (OID = 59469):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT fk_audit_event_type_cd FOREIGN KEY (audit_event_type_cd) REFERENCES audit_event_type(audit_event_type_cd);

-- Definition for index fk_ref_person_id (OID = 59474):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT fk_ref_person_id FOREIGN KEY (ref_person_id) REFERENCES person(person_id);

-- Definition for index fk_alt_ref_person_id (OID = 59479):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT fk_alt_ref_person_id FOREIGN KEY (alt_ref_person_id) REFERENCES person(person_id);

-- Definition for index fk_ref_key_id (OID = 59474):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT fk_ref_key_id FOREIGN KEY (ref_key_id) REFERENCES key(id);

-- Definition for index fk_ref_salt_id (OID = 59479):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT fk_ref_salt_id FOREIGN KEY (ref_salt_id) REFERENCES salt(id);

-- Definition for index fk_creator_id (OID = 59484):
ALTER TABLE ONLY audit_event
    ADD CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for index person_link_review_pkey (OID = 59492):
ALTER TABLE ONLY person_link_review
    ADD CONSTRAINT person_link_review_pkey PRIMARY KEY (person_link_review_id);

-- Definition for index fk_left_person_id:
ALTER TABLE ONLY person_link_review
    ADD CONSTRAINT fk_left_person_id FOREIGN KEY (left_person_id) REFERENCES person(person_id);

-- Definition for index fk_right_person_id:
ALTER TABLE ONLY person_link_review
    ADD CONSTRAINT fk_right_person_id FOREIGN KEY (right_person_id) REFERENCES person(person_id);

-- Definition for index fk_creator_id (OID = 59504):
ALTER TABLE ONLY person_link_review
    ADD CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES app_user(id);

-- Definition for index fk_reviewer_id (OID = 59509):
ALTER TABLE ONLY person_link_review
    ADD CONSTRAINT fk_reviewer_id FOREIGN KEY (reviewer_id) REFERENCES app_user(id);

-- Definition for foreign key fk_dataset_user
ALTER TABLE ONLY dataset
    ADD CONSTRAINT fk_dataset_user FOREIGN KEY (user_id) REFERENCES app_user(id);

-- Definition for unique constraint for dataset table_name
ALTER TABLE ONLY dataset
    ADD CONSTRAINT un_dataset_table_name UNIQUE (table_name);

-- Definition for index fk_column_information_dataset_id:
ALTER TABLE ONLY column_information
    ADD CONSTRAINT fk_column_information_dataset_id FOREIGN KEY (dataset_id) REFERENCES dataset(dataset_id);

-- Definition for index fk_person_pseudo_id:
ALTER TABLE ONLY match_pair_stat_half
    ADD CONSTRAINT fk_person_pseudo_id FOREIGN KEY (person_pseudo_id) REFERENCES person(person_id);

-- Definition for index fk_left_person_pseudo_id:
ALTER TABLE ONLY match_pair_stat
    ADD CONSTRAINT fk_left_person_pseudo_id FOREIGN KEY (left_person_pseudo_id) REFERENCES person(person_id);

-- Definition for index fk_right_person_pseudo_id:
ALTER TABLE ONLY match_pair_stat
    ADD CONSTRAINT fk_right_person_pseudo_id FOREIGN KEY (right_person_pseudo_id) REFERENCES person(person_id);

-- Definition for index fk_column_information_field_type_cd:
ALTER TABLE ONLY column_information
    ADD CONSTRAINT fk_column_information_field_type_cd FOREIGN KEY (field_type_cd) REFERENCES field_type(field_type_cd);

-- Definition for index fk_column_information_field_meaning_cd:
ALTER TABLE ONLY column_information
    ADD CONSTRAINT fk_column_information_field_meaning_cd FOREIGN KEY (field_meaning_cd) REFERENCES field_meaning(field_meaning_cd);

-- Definition for unique field_meaning_code:
ALTER TABLE ONLY field_meaning
    ADD CONSTRAINT un_field_meaning_code UNIQUE (field_meaning_code);

-- Definition for index field_type_code:
ALTER TABLE ONLY field_type
    ADD CONSTRAINT un_field_type_code UNIQUE (field_type_code);


--COMMENT ON SCHEMA public IS 'standard public schema';
--COMMENT ON COLUMN audit_event.ref_person_id IS 'The field refers to a person record that is associated in some way with the audit event. For example in the case of a person record update audit event this field will refer to the person record that was updated.';
--COMMENT ON COLUMN audit_event.alt_ref_person_id IS 'The audit event may refer to a second person that is associated with the event in some way. For example a link audit event would refer to the second person record that was linked.';
--COMMENT ON COLUMN audit_event.ref_key_id IS 'The field refers to a key record that is associated in some way with the audit event. For example in the case of a key record add audit event this field will refer to the key record that was added.';
--COMMENT ON COLUMN audit_event.ref_salt_id IS 'The field refers to a salt record that is associated in some way with the audit event. For example in the case of a salt record add audit event this field will refer to the salt record that was added.';
