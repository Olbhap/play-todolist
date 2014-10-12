package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.Task


object Application extends Controller {
	val taskForm = Form(
	    mapping(
	    	"id" -> ignored(None:Option[Long]),
	      "label" -> nonEmptyText,
	      "task_user" -> nonEmptyText
	    )(Task.apply)(Task.unapply)
  )

	implicit val taskWrites: Writes[Task] = (
	  (JsPath \ "id").write[Option[Long]] and (JsPath \ "label").write[String] and
	  (JsPath \ "task_user").write[String]
	)(unlift(Task.unapply))

	def index = Action{Redirect(routes.Application.tasks)}
 	
	def tasks = Action {
	 val json_lista_task = Json.toJson(Task.all())
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

	def tasksUser(login: String) = Action {
		val json_tasks = Json.toJson(Task.getByUser(login))
		Ok(json_tasks)
	}

	def deleteTask(id: Long) = Action {
		if(Task.delete(id)==0)
	   	NotFound;
	   else
	   	Ok;
	}
}