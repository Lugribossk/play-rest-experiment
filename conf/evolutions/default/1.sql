# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table person (
  id                        integer not null,
  name                      varchar(255) not null,
  rating_profile_id         integer,
  constraint pk_person primary key (id))
;

create table player (
  id                        integer not null,
  person_id                 integer not null,
  tournament_id             integer not null,
  constraint pk_player primary key (id))
;

create table rating_profile (
  id                        integer not null,
  rating                    integer,
  name                      varchar(255),
  constraint pk_rating_profile primary key (id))
;

create table tournament (
  id                        integer not null,
  constraint pk_tournament primary key (id))
;

create sequence person_seq;

create sequence player_seq;

create sequence rating_profile_seq;

create sequence tournament_seq;

alter table person add constraint fk_person_ratingProfile_1 foreign key (rating_profile_id) references rating_profile (id) on delete restrict on update restrict;
create index ix_person_ratingProfile_1 on person (rating_profile_id);
alter table player add constraint fk_player_person_2 foreign key (person_id) references person (id) on delete restrict on update restrict;
create index ix_player_person_2 on player (person_id);
alter table player add constraint fk_player_tournament_3 foreign key (tournament_id) references tournament (id) on delete restrict on update restrict;
create index ix_player_tournament_3 on player (tournament_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists person;

drop table if exists player;

drop table if exists rating_profile;

drop table if exists tournament;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists person_seq;

drop sequence if exists player_seq;

drop sequence if exists rating_profile_seq;

drop sequence if exists tournament_seq;

