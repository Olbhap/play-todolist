# --- !Ups

alter table task add task_categoria integer;
CREATE SEQUENCE cat_id_seq;

CREATE TABLE categoria (
    id integer NOT NULL DEFAULT nextval('cat_id_seq'),
    nombre varchar(50) NOT NULL,
    task_user varchar(50) NOT NULL,
    constraint pk_cat primary key (id)
);

alter table task add constraint fk_task_categoria_1 foreign key (task_categoria) references categoria (id) on delete restrict on update restrict;
alter table categoria add constraint fk_taskuser_categoria_1 foreign key (task_user) references task_user (login) on delete restrict on update restrict;


insert into categoria (nombre,task_user) values ('Tareas Domingo','domingogallardo');
insert into categoria (nombre,task_user) values ('Tareas Pablo','pablogil');


insert into task (label, task_user, task_categoria) values('Intentar hacer que funcionen las categorias','pablogil','1');
insert into task (label, task_user, task_categoria) values('Comprobar las categorias','pablogil','1');
insert into task (label, task_user, task_categoria) values('Corregir a Pablo','domingogallardo','2');


# --- !Downs 
DROP TABLE categoria;
DROP SEQUENCE cat_id_seq;