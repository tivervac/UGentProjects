# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table parser_task (
  id                        bigint not null,
  status                    integer,
  result                    TEXT,
  version                   bigint not null,
  constraint ck_parser_task_status check (status in (0,1,2,3,4,5,6,7,8,9,10)),
  constraint pk_parser_task primary key (id))
;

create sequence parser_task_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists parser_task;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists parser_task_seq;

