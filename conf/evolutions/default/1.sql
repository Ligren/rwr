# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table APPLICANTS (
  id                        integer auto_increment not null,
  name                      varchar(255),
  date_interview            timestamp,
  date_addition             timestamp not null,
  constraint pk_APPLICANTS primary key (id))
;

create table contact (
  id                        bigint not null,
  name                      varchar(255),
  title                     varchar(255),
  email                     varchar(255),
  type_contact_id           integer,
  owner_id                  integer,
  value                     varchar(255),
  constraint pk_contact primary key (id))
;

create table rating (
  id                        integer auto_increment not null,
  skill_id                  integer,
  owner_id                  integer,
  value                     integer(3) not null,
  constraint pk_rating primary key (id))
;

create table skill (
  id                        integer auto_increment not null,
  name                      varchar(50) not null,
  constraint pk_skill primary key (id))
;

create table type_contact (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_type_contact primary key (id))
;

create sequence contact_seq;

create sequence type_contact_seq;

alter table contact add constraint fk_contact_typeContact_1 foreign key (type_contact_id) references type_contact (id) on delete restrict on update restrict;
create index ix_contact_typeContact_1 on contact (type_contact_id);
alter table contact add constraint fk_contact_owner_2 foreign key (owner_id) references APPLICANTS (id) on delete restrict on update restrict;
create index ix_contact_owner_2 on contact (owner_id);
alter table rating add constraint fk_rating_skill_3 foreign key (skill_id) references skill (id) on delete restrict on update restrict;
create index ix_rating_skill_3 on rating (skill_id);
alter table rating add constraint fk_rating_owner_4 foreign key (owner_id) references APPLICANTS (id) on delete restrict on update restrict;
create index ix_rating_owner_4 on rating (owner_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists APPLICANTS;

drop table if exists contact;

drop table if exists rating;

drop table if exists skill;

drop table if exists type_contact;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists contact_seq;

drop sequence if exists type_contact_seq;

