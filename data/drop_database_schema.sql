------------------------------------------
--Host     : localhost
--Database : openempi



SET SESSION AUTHORIZATION 'openempi';
SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.person DROP CONSTRAINT fk_created_by_app_user;
ALTER TABLE ONLY public.user_session DROP CONSTRAINT fk_user_session_user;
ALTER TABLE ONLY public.user_role DROP CONSTRAINT fk_user_role_role;
ALTER TABLE ONLY public.user_role DROP CONSTRAINT fk_user_role_user;
ALTER TABLE ONLY public.person_link DROP CONSTRAINT fk_created_by_app_user;
ALTER TABLE ONLY public.person_link DROP CONSTRAINT fk_left_person_id;
ALTER TABLE ONLY public.person_link DROP CONSTRAINT fk_right_person_id;
ALTER TABLE ONLY public.person_link DROP CONSTRAINT fk_person_match_id;
ALTER TABLE ONLY public.person_match DROP CONSTRAINT fk_created_by_app_user_match;
ALTER TABLE ONLY public.person_match DROP CONSTRAINT fk_left_dataset_id;
ALTER TABLE ONLY public.person_match DROP CONSTRAINT fk_right_dataset_id;
ALTER TABLE ONLY public.person_match_request DROP CONSTRAINT fk_created_by_app_user_match_request;
ALTER TABLE ONLY public.person_match_request DROP CONSTRAINT fk_person_match_request_person_match_id;
ALTER TABLE ONLY public.person_match_request DROP CONSTRAINT fk_person_match_request_dataset_id;
ALTER TABLE ONLY public.column_match_information DROP CONSTRAINT fk_column_match_information_person_match_id;
ALTER TABLE ONLY public.column_match_information DROP CONSTRAINT fk_column_match_information_field_type_cd;
ALTER TABLE ONLY public.column_match_information DROP CONSTRAINT fk_column_match_information_field_meaning_cd;
ALTER TABLE ONLY public.column_information DROP CONSTRAINT fk_column_information_dataset_id;
ALTER TABLE ONLY public.column_information DROP CONSTRAINT fk_column_information_field_type_cd;
ALTER TABLE ONLY public.column_information DROP CONSTRAINT fk_column_information_field_meaning_cd;
ALTER TABLE ONLY public.match_pair_stat_half DROP CONSTRAINT fk_person_pseudo_id;
ALTER TABLE ONLY public.match_pair_stat DROP CONSTRAINT fk_left_person_pseudo_id;
ALTER TABLE ONLY public.match_pair_stat DROP CONSTRAINT fk_right_person_pseudo_id;
ALTER TABLE ONLY public.key DROP CONSTRAINT fk_key_created_by_app_user;
ALTER TABLE ONLY public.key DROP CONSTRAINT fk_key_voided_by_app_user;
ALTER TABLE ONLY public.salt DROP CONSTRAINT fk_salt_created_by_app_user;
ALTER TABLE ONLY public.salt DROP CONSTRAINT fk_salt_voided_by_app_user;
ALTER TABLE ONLY public.user_session DROP CONSTRAINT user_session_pkey;
ALTER TABLE ONLY public.user_role DROP CONSTRAINT user_role_pkey;
ALTER TABLE ONLY public."role" DROP CONSTRAINT role_pkey;
ALTER TABLE ONLY public.person_link DROP CONSTRAINT person_link_pkey;
ALTER TABLE ONLY public.person_match DROP CONSTRAINT person_match_pkey;
ALTER TABLE ONLY public.column_match_information DROP CONSTRAINT column_match_information_pkey;
ALTER TABLE ONLY public.column_information DROP CONSTRAINT column_information_pkey;
ALTER TABLE ONLY public.field_type DROP CONSTRAINT field_type_pkey;
ALTER TABLE ONLY public.field_meaning DROP CONSTRAINT field_meaning_pkey;
ALTER TABLE ONLY public.person DROP CONSTRAINT person_pkey CASCADE;
ALTER TABLE ONLY public.match_pair_stat_half DROP CONSTRAINT match_pair_stat_half_pkey;
ALTER TABLE ONLY public.match_pair_stat DROP CONSTRAINT match_pair_stat_pkey;
ALTER TABLE ONLY public.key DROP CONSTRAINT key_pkey CASCADE;
ALTER TABLE ONLY public.salt DROP CONSTRAINT salt_pkey CASCADE;
ALTER TABLE ONLY public.app_user DROP CONSTRAINT app_user_email_key;
ALTER TABLE ONLY public.app_user DROP CONSTRAINT app_user_username_key;
ALTER TABLE ONLY public.app_user DROP CONSTRAINT app_user_pkey CASCADE;
ALTER TABLE ONLY public.audit_event DROP CONSTRAINT fk_audit_event_type_cd;
ALTER TABLE ONLY public.audit_event_type DROP CONSTRAINT idx_audit_event_type_name;
ALTER TABLE ONLY public.audit_event_type DROP CONSTRAINT audit_event_type_pkey;
ALTER TABLE ONLY public.audit_event DROP CONSTRAINT audit_event_pkey;


DROP INDEX public.user_session_date_created;
DROP INDEX public.user_session_session_key;
DROP INDEX public.role_name;
DROP INDEX public.person_link_left_person_id;
DROP INDEX public.person_link_right_person_id;
DROP INDEX public.person_link_weight;
DROP INDEX public.person_match_left_dataset_id;
DROP INDEX public.person_match_right_dataset_id;
DROP INDEX public.person_match_request_dataset_id;
DROP INDEX public.column_match_information_person_match_id;
DROP INDEX public.column_information_dataset_id;
DROP INDEX public.match_pair_stat_half_id;
DROP INDEX public.match_pair_stat_half_person_pseudo_id;
DROP INDEX public.match_pair_stat_id;
DROP INDEX public.idx_audit_event_type_code;
DROP INDEX public.idx_audit_event_ref_person;

DROP SEQUENCE public.user_session_seq;
DROP SEQUENCE public.person_seq;
DROP SEQUENCE public.person_link_seq;
DROP SEQUENCE public.person_match_seq;
DROP SEQUENCE public.person_match_request_seq;
DROP SEQUENCE public.column_match_information_seq;
DROP SEQUENCE public.match_pair_stat_half_seq;
DROP SEQUENCE public.match_pair_stat_seq;
DROP SEQUENCE public.key_seq;
DROP SEQUENCE public.salt_seq;
DROP SEQUENCE public.hibernate_sequence;
DROP SEQUENCE public.audit_event_seq;
DROP SEQUENCE public.person_link_review_seq;
DROP SEQUENCE public.dataset_seq;
DROP SEQUENCE public.column_information_seq;

DROP TABLE public.person;
DROP TABLE public.key;
DROP TABLE public.salt;
DROP TABLE public.user_session;
DROP TABLE public.user_role;
DROP TABLE public."role";
DROP TABLE public.person_link_review;
DROP TABLE public.audit_event;
DROP TABLE public.audit_event_type;
DROP TABLE public.person_link;
DROP TABLE public.person_match;
DROP TABLE public.person_match_request;
DROP TABLE public.match_pair_stat_half;
DROP TABLE public.match_pair_stat;
DROP TABLE public.field_type;
DROP TABLE public.field_meaning;
DROP TABLE public.column_match_information;
DROP TABLE public.dataset;
DROP TABLE public.column_information;
DROP TABLE public.app_user;

