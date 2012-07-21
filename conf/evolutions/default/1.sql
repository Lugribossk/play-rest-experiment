# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table person (
  id                        integer not null,
  name                      varchar(255) not null,
  email                     varchar(255),
  constraint pk_person primary key (id))
;

create table player (
  id                        integer not null,
  person_id                 integer not null,
  tournament_id             integer not null,
  constraint pk_player primary key (id))
;

create table tournament (
  id                        integer not null,
  name                      varchar(255) not null,
  constraint pk_tournament primary key (id))
;

create table user (
  id                        integer not null,
  username                  varchar(255) not null,
  hashed_password           varchar(255) not null,
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;


create table tournament_user (
  tournament_id                  integer not null,
  user_id                        integer not null,
  constraint pk_tournament_user primary key (tournament_id, user_id))
;
create sequence person_seq;

create sequence player_seq;

create sequence tournament_seq;

create sequence user_seq;

alter table player add constraint fk_player_person_1 foreign key (person_id) references person (id) on delete restrict on update restrict;
create index ix_player_person_1 on player (person_id);
alter table player add constraint fk_player_tournament_2 foreign key (tournament_id) references tournament (id) on delete restrict on update restrict;
create index ix_player_tournament_2 on player (tournament_id);



alter table tournament_user add constraint fk_tournament_user_tournament_01 foreign key (tournament_id) references tournament (id) on delete restrict on update restrict;

alter table tournament_user add constraint fk_tournament_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists person;

drop table if exists player;

drop table if exists tournament;

drop table if exists tournament_user;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists person_seq;

drop sequence if exists player_seq;

drop sequence if exists tournament_seq;

drop sequence if exists user_seq;

