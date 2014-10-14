# Tasks schema
 
# --- !Ups

alter table task add end_date date;
insert into task (label, task_user, end_date) values('Prueba de fecha opcional','pablogil','2014-10-10');
   insert into task (label, task_user, end_date) values('Prueba de fecha opcional2','pablogil','2024-10-10');
 
# --- !Downs
 
