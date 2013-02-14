ALTER TABLE public.person
	ADD COLUMN account varchar(255);

ALTER TABLE public.person
	ADD COLUMN account_identifier_domain_id integer;

ALTER TABLE public.person
	ADD CONSTRAINT fk_account_identifier_domain_id
	FOREIGN KEY (account_identifier_domain_id)
		REFERENCES public.identifier_domain(identifier_domain_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION;

ALTER TABLE public.person
	ADD COLUMN custom6 varchar(255),
	ADD COLUMN custom7 varchar(255),
	ADD COLUMN custom8 varchar(255),
	ADD COLUMN custom9 varchar(255),
	ADD COLUMN custom10 varchar(255),
	ADD COLUMN custom11 varchar(255),
	ADD COLUMN custom12 varchar(255),
	ADD COLUMN custom13 varchar(255),
	ADD COLUMN custom14 varchar(255),
	ADD COLUMN custom15 varchar(255),
	ADD COLUMN custom16 varchar(255),
	ADD COLUMN custom17 varchar(255),
	ADD COLUMN custom18 varchar(255),
	ADD COLUMN custom19 varchar(255),
	ADD COLUMN custom20 varchar(255),
	ADD COLUMN group_number varchar(64);

-- Structure for table phone_type:
CREATE TABLE phone_type (
    phone_type_cd integer NOT NULL,
    phone_type_name varchar(64) NOT NULL,
    phone_type_description varchar(255),
    phone_type_code varchar(64) NOT NULL
) WITHOUT OIDS;

insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (1, 'Beeper Number', 'Beeper Number or paging device suitable to solicit or to leave a very short message', 'B');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (2, 'Cellular Phone Number', 'Cellular Phone Number', 'C');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (3, 'E-mail Address', 'E-mail Address', 'E');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (4, 'Fax Number', 'Fax Number', 'F');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (5, 'Home Phone Number', 'Home Phone Number', 'H');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (6, 'Office Phone Number', 'Office Phone Number', 'O');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (7, 'Primary Home Number', 'The primary home to reach a person after business hours', 'HP');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (8, 'Vacation Home Number', 'A vacation home to reach a person while on vacation', 'HV');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (9, 'Answering Service', 'An automated answering machine used for less urgent cases', 'AS');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (10, 'Emergency Number', 'A contact specifically designated to be used for emergencies. This is the first choice in emergencies, independent of any other use codes.', 'EC');
insert into phone_type (phone_type_cd, phone_type_name, phone_type_description, phone_type_code) values (11, 'Unknown', 'Phone type is unknown.', 'UN');

ALTER TABLE ONLY phone_type
    ADD CONSTRAINT phone_type_pkey PRIMARY KEY (phone_type_cd);

ALTER TABLE public.person
	ADD COLUMN phone_type_cd integer;

ALTER TABLE ONLY person
    ADD CONSTRAINT fk_phone_type FOREIGN KEY (phone_type_cd) REFERENCES phone_type(phone_type_cd);

