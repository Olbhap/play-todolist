package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.{Date}
import java.text.SimpleDateFormat
import models._


object Application extends Controller {
	val taskForm = Form(
	    mapping(
	    	"id" -> ignored(None:Option[Long]),
	      "label" -> nonEmptyText,
	      "task_user" -> nonEmptyText,
	      "end_date" -> optional(date)
	    )(Task.apply)(Task.unapply)
  )

	val catForm = Form(
	    mapping(
	    	"id" -> ignored(None:Option[Long]),
	      "nombre" -> nonEmptyText,
	      "task_user" -> nonEmptyText	      
	    )(Categoria.apply)(Categoria.unapply)
  )
	
	implicit val taskWrites: Writes[Task] = (
	  (JsPath \ "id").write[Option[Long]] and (JsPath \ "label").write[String] and
	  (JsPath \ "task_user").write[String] and (__ \ "end_date").write[Option[Date]]
	)   (unlift(Task.unapply))

	implicit val categoriaWrites: Writes[Categoria] = (
	  (JsPath \ "id").write[Option[Long]] and (JsPath \ "nombre").write[String] and
	  (JsPath \ "task_user").write[String])  (unlift(Categoria.unapply))

	def index = Action{Redirect(routes.Application.tasks)}
 	
	def tasks = Action {
	 val json_lista_task = Json.toJson(Task.getByUser("anon"))
	 Ok(json_lista_task)
	}

	def task(id: Long) = Action {
		val json_task = Json.toJson(Task.getById(id))
		Ok(json_task)
	}

	 def newTask = newTaskUser("anonymous")

	 def newTaskUser(user: String) = Action { implicit request =>
	 	println(user);
     taskForm.bindFromRequest.fold(
       errors => BadRequest("Error en la peticion"),
       taskData =>       
       if (User.exists(taskData.task_user)) {
       				println(taskData.label + " " + user)
                   val id: Long = Task.create(taskData.label, taskData.task_user, taskData.end_date)
                   val task = Task.getById(id)
                   Created(Json.toJson(Task.getById(id)))
                }
                else BadRequest("Error: No existe el propietario de la tarea: " + taskData.task_user)
     )
   }

	def tasksUser(login: String) = Action {
		val taskList = Task.getByUser(login)
		if(taskList.isEmpty)
			NotFound
		else
		{
			Ok(Json.toJson(taskList))
		}		
	}

	def taskUserCustomDate(login: String, fecha: String) = Action {
		val taskList = Task.getByUserCustomDate(login, fecha)
		if(taskList.isEmpty)
			NotFound
		else
		{
			Ok(Json.toJson(taskList))
		}
	}

	def taskUserDate(login: String) = Action {
		val taskList = Task.getByUserDate(login)
		if(taskList.isEmpty)
			NotFound
		else
		{
			Ok(Json.toJson(taskList))
		}
	}

	def deleteTask(id: Long) = Action {
		if(Task.delete(id)==0)
	   	NotFound;
	   else
	   	Ok;
	}

	def deleteCat(id: Long) = Action {
		if(Categoria.delete(id)==0)
	   	NotFound;
	   else
	   	Ok;
	}

	def taskUserCat(login: String, categoria: Long) = Action {
		val taskList = Task.getByUserCat(login, categoria)
		if(taskList.isEmpty)
			NotFound
		else
		{
			Ok(Json.toJson(taskList))
		}
	}

	def newCategoria(login: String)  = Action { implicit request =>
     catForm.bindFromRequest.fold(
       errors => BadRequest("Error en la peticion"),
       catData =>       
       if (User.exists(login)) {
       				
                   val id: Long = Categoria.create(catData.nombre, catData.task_user)
                   val cate = Categoria.getById(id)
                   Created(Json.toJson(cate))
                }
                else BadRequest("Error: No existe el propietario de la tarea: " + catData.task_user)
     )
   }
}