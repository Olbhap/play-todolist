Play-todoList - P1 - *Pablo Gil Carpio*
===================
----------
Play-todoList está desarrollada bajo el framework *Play* y empleando el lenguaje *scala*. En esta primera práctica convertiremos la aplicación en un *API REST*, via *JSON*.

**Tabla de contenidos**

[TOC]

----------
Funcionalidades
-------------
> **/tasks**

> - **GET**
> - Devuelve una colección *JSON* con la lista de tareas del usuario sin registrar: *Anon*
> - No requiere ningún parámetro. Invoca a la función *controllers.Application.tasks* (vease la sección [Controlador](#Controlador))

> **/:login/tasks  **

> - **GET**
> - Devuelve una colección *JSON* con la lista de tareas del usuario :login.
> - Recibe un **string** :login con el nombre del usuario. Invoca a la función *controllers.Application.tasksUser* (vease la sección [ Controlador](#Controlador))
> - Si el usuario no existe, devolverá **404** de status code. **200** en el caso de que si exista

> **/tasks/:id **

> - **GET**
> - Devuelve un objeto *JSON*  de una tarea según su :ID.
> - Recibe un long **:id** con el ID de la tarea. Invoca a la función *controllers.Application.tasks* con el parámetro ID (vease la sección [ Controlador](#Controlador))
> - Devuelve **200** en el caso de que exista

> **/:login/:fecha/tasks ** - Función propia de *Feature3*

> - **GET**
> - Devuelve todas las tareas sin finalizar a partir de una fecha en formato *(dd-MM-yyyy)* y de un usuario.
> - Recibe un String **:login** y un String **:fecha** en el formato mencionado. Invoca a la función *controllers.Application.taskUserCustomDate* (vease la sección [ Controlador](#Controlador))
> - Devuelve **200** en el caso de que existan tareas, **404** en caso contrario

> **/:login/tasks/now ** - Función propia de *Feature3*

> - **GET**
> - Devuelve todas las tareas sin finalizar a partir de la fecha del sistema y de un usuario
> - Recibe un String **:login** y un String **:fecha** en el formato mencionado. Invoca a la función *controllers.Application.taskUserCustomDate* (vease la sección [ Controlador](#Controlador))
> - Devuelve **200** en el caso de que existan tareas, **404** en caso contrario

---
> **/tasks ** 

> - **POST**
> - Crea una nueva tarea
> - No recibe ningún parámetro, por lo que creará la tarea en el usuario 'Anon'. Invoca a la función *controllers.Application.newTasks* (vease la sección [ Controlador](#Controlador))
> - Devuelve **201** si todo fue correctamente.

> **/:login/tasks** 

> - **POST**
> - Crea una nueva tarea asignada a un usuario
> - Recibe un String **:login** . Invoca a la función *controllers.Application.newTaskUser* (vease la sección [ Controlador](#Controlador))
> - Devuelve **201** si todo fue correctamente.

---
> **/tasks/:id** 

> - **DELETE**
> - Elimina una tarea por ID
> - Recibe un long **:id** con el ID de la tarea. Invoca a la función *controllers.Application.newTasks* (vease la sección [ Controlador](#Controlador))
> - Devuelve **201** si todo fue correctamente.



Controlador
-------------

En el controlador de la aplicación definiremos todas las funciones a las que el fichero routes enlaza, además de declarar los **Writer** del objeto JSON que devolvemos en la mayoría de las funciones GET.

####  JSON

El objeto JSON está determinado por lo siguiente:

 - ID de la tarea
 - label
 - task_user
 - end_date

Configurando el **Writer** del objeto **Task** se determina esta configuración de JSON

#### <i class="icon-hdd"></i> TaskForm

Aquí se declara el mapeado (mapping) del formulario que construirá un objeto Task. 
En él se declara los parámetros que son opcionales (Como por ejemplo, la fecha)

> **Tip:** Se utiliza el tipo *Option* de Scala para poder definir valores que pueden ser del tipo *[A]* o nulos.

El resto de las funciones invocan a funciones del modelo Task.scala y devuelven los status code mencionados en el apartado anterior.

Modelos y DB Tables
-------------
En la práctica 1 tenemos dos tablas en la base de datos: task y task_user, pero debido a que la tabla de usuarios no es más que una referencia, no es necesario crear su modelo, por lo que solo mencionaré el modelo Task.scala

###Database
|        task | Type              
 ----------------- | ---------------------------- 
| id | Integer *(PK)*
| label | varchar(255)           
| task_user           | varchar(50)

|        task_user| Type              
 ----------------- | ---------------------------- 
| login | varchar(50) *(PK)*


###Model - Task
En el modelo se declaran las funciones que harán los trabajos con la base de datos. 
Aquí se describen implicitamente las select SQL, devolviendo normalmente una lista de Task que nos devuelve la base datos, por ejemplo:

    def getByUserCustomDate(login: String, fecha: String): List[Task] = 
    	DB.withConnection { 
    		val date_parse = new SimpleDateFormat("dd-MM-yyyy").parse(fecha)
    		implicit c =>
    	  SQL("select * from task where task_user = {login} and end_date > {date_parse}").on('login -> login, 'date_parse - date_parse).as(task *)
    	}
Podemos observar la SQL Select. En este caso es la función que devuelve todas las tareas que finalizan a partir de la fecha que el usuario pasa por parámetro. 
Para ello, utilizamos la clase **SimpleDateFormat** de *java.text* para poder darle formato al string y que nuestro servidor de bases de datos sepa interpretarlo correctamente.

La otra función propia utilizando fechas es la siguiente:

    def getByUserDate(login: String): List[Task] = 
    	DB.withConnection { 
    		implicit c =>
    	  SQL("select * from task where task_user = {login} and end_date > NOW()").on('login -> login).as(task *)
    }
En esta ocasión, la fecha con la que compararemos la determina el propio servidor de bases de datos mediante la función NOW() de SQL.

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

####Un ejemplo: *3.sql*
 

    # --- !Ups
    
    alter table task add end_date date;
    
    insert into task (label, task_user, end_date) values('Prueba de fecha opcional','pablogil','2014-10-10');
    
    insert into task (label, task_user, end_date) values('Prueba de fecha opcional2','pablogil','2024-10-10');
     
    # --- !Downs