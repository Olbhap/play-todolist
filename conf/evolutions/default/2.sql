# --- !Ups

alter table task add task_user varchar(50);

CREATE TABLE task_user (
    login varchar(50) NOT NULL,
    constraint pk_taskuser primary key (login)
);

alter table task add constraint fk_task_task_user_1 foreign key (task_user) references task_user (login) on delete restrict on update restrict;
create index ix_computer_company_1 on task (task_user);

insert into task_user (login) values ('domingogallardo');
insert into task_user (login) values ('pablogil');
insert into task_user (login) values ('pepito');
insert into task_user (login) values ('apple');
insert into task_user (login) values ('madslover');
insert into task_user (login) values ('restfulswagger');

# --- !Downs 
DROP TABLE task_user;