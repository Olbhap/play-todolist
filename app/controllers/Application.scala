package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.{Date}
import java.text.SimpleDateFormat
import models.Task


object Application extends Controller {
	val taskForm = Form(
	    mapping(
	    	"id" -> ignored(None:Option[Long]),
	      "label" -> nonEmptyText,
	      "task_user" -> nonEmptyText,
	      "end_date" -> optional(date)
	    )(Task.apply)(Task.unapply)
  )
	
	implicit val taskWrites: Writes[Task] = (
	  (JsPath \ "id").write[Option[Long]] and (JsPath \ "label").write[String] and
	  (JsPath \ "task_user").write[String] and (__ \ "end_date").write[Option[Date]]
	)(unlift(Task.unapply))

	def index = Action{Redirect(routes.Application.tasks)}
 	
	def tasks = Action {
	 val json_lista_task = Json.toJson(Task.getByUser("anon"))
	 Ok(json_lista_task)
	}

	def task(id: Long) = Action {
		val json_task = Json.toJson(Task.getById(id))
		Ok(json_task)
	}

	def newTask = Action { implicit request =>
	  taskForm.bindFromRequest.fold(
	    errors => BadRequest(views.html.index(Task.all(), errors)),
	    task_user => {
	      Task.create(task_user)
	      Created(Json.toJson(task_user))
	    }
	  )
	}

	def newTaskUser(login: String) = Action { implicit request =>
	  taskForm.bindFromRequest.fold(
	    errors => BadRequest(views.html.index(Task.all(), errors)),
	    task_user => {	    	
	      Task.create(task_user)
	      Created(Json.toJson(task_user))
	    }
	  )
	}	

	def tasksUser(login: String) = Action {
		val taskList = Task.getByUser(login)
		println(taskList.last.end_date.toString);
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
}