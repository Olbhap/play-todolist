Play-todoList - P2 - *Pablo Gil Carpio*
===================
----------
Play-todoList está desarrollada bajo el framework *Play* y empleando el lenguaje *scala*. En esta segunda práctica utilizaremos el framework de pruebas *specs2*.

**Tabla de contenidos**

[TOC]

----------
Pruebas de la aplicación
-------------

####Modelo
--------

##### **Pruebas del modelo Task**
> 
> **Create without DATE and retrieve by id**

> - Simplemente crea una tarea de prueba para el usuario *"pablogil"*
> - Una vez creada la tarea, la recuperamos y comprobamos si efectivamente se ha creado y el modelo trabaja correctamente con la DB

> **Create with DATE and retrieve by id**

> - Crea una tarea de prueba para el usuario *"pablogil"* y con una fecha concreta
> - Una vez creada la tarea, la recuperamos y comprobamos si efectivamente se ha creado y el modelo trabaja correctamente con la DB

> **Delete if exists**

> - Crea una tarea de prueba
> - Recupera la tarea 

> **Retrieve by custom date** - Testeo de *Feature3*

> - Realizamos una petición a la DB mediante el modelo, filtrando por tarea
> - Con el usuario "pablogil" y la fecha "11-10-2013" deberá aparecer 2 tareas
> - Comprobamos que efectivamente el listado de tareas recuperadas tiene un tamaño de 2

> **Retrieve by user** - Testeo de *Feature2*

> - Realizamos una petición al modelo bajo el filtro de usuario
> - Recuperamos todas las tareas creadas por el usuario "domingogallardo"
> - Según nuestra DB, deberá aparecer 4 tareas, comprobamos y vemos que efectivamente es así.

> **Retrieve by user NOW** - Testeo de *Feature3*

> - Petición al modelo bajo el filtro de usuario + fecha actual del sistema
> - Recuperaremos las tareas cuya fecha sea superior a la fecha del sistema para el usuario "pablogil"
> - Según nuestra DB, deberá aparecer 1 tarea, comprobamos y vemos que efectivamente es así.

---
##### **Pruebas del modelo Categoria**
> **Retrieve by categoria 1** 

> - Recuperamos todas las categorias del usuario pablogil y cuya id de categoria es 1
> - La base de datos tiene dos tareas bajo estos filtros.
> - Se comprueba que efectivamente el tamaño de la lista de tareas que nos devuelve el modelo es 2

> **Retrieve by categoria 2** 

> - Recuperamos todas las categorias del usuario domingogallardo y cuya id de categoria es 2
> - La base de datos tiene una tarea bajo este filtro.
> - Se comprueba que efectivamente el tamaño de la lista de tareas que nos devuelve el modelo es 1

> **Create and retrieve by id** 

> - Creamos una categoria de prueba para el usuario "pablogil"
> - Recuperamos esta categoría
> - Comprobamos que efectivamente los datos de la categoría recuperada coinciden con los indicados a la hora de crearse (nombre y usuario)

En el testeo del modelo de categoría comprobamos que toda la interacción del modelo con la base de datos es correcta y que, una vez aplicados los diferentes filtros, devuelve la información que debe devolver.

#### Controlador
##### **Pruebas del controlador**

El controlador se encarga de recibir las llamadas declaradas en el fichero de rutas bajo la url indicada.
A continuación, gestionará las llamadas a los diferentes modelos y, en el caso de nuestra API REST, devolverá los objetos JSON necesarios, así como el STATUS CODE que deba, indicando así si se ha encontrado algún error en el procesado de la petición.

Tal y como se muestra en el fichero *ControllersSpect.scala*, el controlador debería:
> **Devolver una tarea en formato JSON con un GET /tasks/id** 

> - En este test se realiza una petición al servidor (una *FakeRequest*) de tipo get a la URL /tasks/id
> - El resultado del test es correcto si el *STATUS* es OK y si, además, el json que nos devuelve el servidor coincide con el que debería ser, por lo que se realiza una comprobación.

> **Tareas de un User en formato JSON con un GET /login/tasks** 

> - Este test realizará una petición al servidor para recuperar las tareas bajo el filtro de un usuario
> - En nuestro caso, para el usuario "pepito" creamos 3 tareas (pepito inicialmente está vacio)
> - Una vez creadas las tareas, las recuperamos mediante petición GET a /usuario/tasks
> - El servidor, si todo está correcto, devolverá STATUS OK y una lista de tareas en formato JSON
> - Recorremos la lista JSON y comprobamos que los label de cada una de las tareas creadas coinciden

> **Devolver todas las tareas sin finalizar a partir de una fecha (dd-MM-yyyy) y de un usuario en JSON con un GET /login/fecha/tasks** 

> - Como el test anterior, pero añadido un filtro más: La fecha
> - Comprobamos que bajo los filtros indicados devuelve el numero de tareas exacto, además de coincidir los label de cada uno de los elementos de la lista JSON recibida por el servidor.
> - Para comprobar que funciona del todo correctamente, se añade una tarea más con fecha superior a las anteriores.
> - Entonces se realiza una petición con una fecha intermedia entre las primeras y la anterior, por lo que solo deberá devolver una tarea.
> - Comprobamos que efectivamente esto es así

> **Devolver todas las tareas sin finalizar a partir de hoy(NOW) y de un usuario en JSON con un GET /login/tasks/now** 

> - En este caso, el filtro hora lo establece el propio sistema, por lo que repetiremos el proceso del test anterior pero con fecha actual
> - Comprobamos también que añadiendo una tarea extra con fecha anterior a hoy, no aparece cuando se realiza la petición al servidor, devolviendo la cantidad de tareas que debe y demostrando el correcto funcionamiento.

> **Create TAREA by POST /user/tasks**

> - Metodo POST para la creación de una tarea asociada a un usuario
> - Realizamos una petición POST al servidor, creando un formulario con el campo label y usuario requerido
> - Si existe el usuario y hemos creado el formulario simulado de forma correcta, el servidor devolverá STATUS CREATED
> - En cambio, si le enviamos un usuario que no existe, o el formulario no está completo, comprobamos que efectivamente el servidor nos devuelve STATUS BAD REQUEST

> **Tareas que pertenecen a un usuario y una categoria formato JSON con un GET /categoria/login/categoria**

> - Testeo mediante petición GET al servidor bajo los filtros USUARIO y CATEGORIA.
> - El servidor nos devolverá las tareas que coincidan con esos filtros.
> - Comprobamos que el STATUS sea OK y que los label de la lista de tareas sea el correcto

> **Create CATEGORIA by POST /user/tasks**

> - Testeo de una petición POST que crea una categoria. Una categoría la crea el usuario, por lo que a la petición POST le pasamos via formulario en el BODY el nombre de la categoria y un usuario asociado.
> - Comprobamos que efectivamente el servidor nos devuelve STATUS CREATED


Modelos y DB Tables de Categoría
-------------
En la práctica 2 se pedía realizar una nueva característica (*feature4*): Las categorías
Se debe ampliar el API para que se puedan crear categorías asociadas a un usuario y que se puedan añadir, modificar y listar las tareas de un usuario dentro de una determinada categoría.


###Database
|        Categoría| Type              
 ----------------- | ---------------------------- 
| id | Integer *(PK)*
| nombre| varchar(50)           
| task_user           | varchar(50)
Además, contiene una *foreign key* hacia la tabla Task_User

También se han actualizado las tabla Task para reflejar la inserción de las Categorías en el sistema

|        Task| Type              
 ----------------- | ---------------------------- 
| task_categoria| Integer
Añadido también una *foreign key* de Task hacia Categoría para vincular las tareas a categorías


###Model - Categoria
En el modelo se declaran las funciones que harán los trabajos con la base de datos. 
Aquí se describen implicitamente las select SQL, devolviendo normalmente una lista de Categoría que nos devuelve la base datos, por ejemplo:

    def getById(id: Long) = DB.withConnection{ implicit c=>
      SQL("select * from categoria where id = {id}").on('id -> id).as(categoria *)
   }
Podemos observar la SQL Select. En este caso es la función que devuelve la categoría bajo el filtro de su ID

La otra función por ejemplo es el create

    def create(nombre: String, task_user: String): Long = {
      DB.withConnection { implicit c =>
         val id: Option[Long]  = 
            SQL("insert into categoria (nombre, task_user) values ({nombre}, {task_user})").on(
               'nombre -> nombre,
               'task_user -> task_user
            ).executeInsert()

         //Devolvemos -1 si el insert devuelve None
         id.getOrElse(-1)
     }
    }

En esta ocasión, se realiza un insert en la tabla categoria bajo un nombre de categoría y un usuario asociado

Evolutions
-------------
En *Play Framework* una de las formas de crear las tablas y filas de las bases de datos es usando *evoluciones*,
Un fichero evolution contiene dos partes básicas:

    # --- !Ups
    inserts/alters/creates...

    # --- !Downs
    deletes


 1. En la primera evolución declaramos la tabla task
 2. En la segunda, a la tabla task añadimos la columna user_task a task, creamos la tabla task y añadimos filas de ejemplos para ambas tablas
 3. En la tercera, añadimos la columna end_date a task y añadimos dos ejemplos de tareas con fecha.
 4. En esta cuarta evolución, hemos añadido la tabla categoría, un par de filas de prueba, así como tareas asociadas a categorías y modificaciones al resto de tablas para que todo el ecosistema funcione como debe.

####*4.sql*
 

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