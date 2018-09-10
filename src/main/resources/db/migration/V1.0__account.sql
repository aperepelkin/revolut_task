create sequence account_id_seq;

create table accounts (
  id bigint primary key not null,
  number varchar(10) not null unique,
  balance decimal(20, 2) default 0
);