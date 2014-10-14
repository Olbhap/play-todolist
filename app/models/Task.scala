package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.{Date}

case class Task(id: Option[Long] = None, label: String, task_user: String, end_date: Option[Date])



object Task{
		val task = {
	  get[Option[Long]]("id") ~ 
	  get[String]("label") ~
 	  get[String]("task_user")~
 	  get[Option[Date]]("end_date") map {
	    case id~label~task_user~end_date => Task(id, label,task_user, end_date)
	  }
	}
	
	def all(): List[Task] = DB.withConnection { implicit c =>
	  SQL("select * from task").as(task *)
	}

	def getByUser(login: String): List[Task] = DB.withConnection { implicit c =>
	  SQL("select * from task where task_user = {login}").on('login -> login).as(task *)
	}

	def getById(id: Long) = DB.withConnection{ implicit c=>
		SQL("select * from task where id = {id}").on('id -> id).as(task *)
	}

	def create(task: Task) {
	  DB.withConnection { implicit c =>
	    SQL("insert into task (label,task_user, end_date) values ({label}, {task_user}, {end_date})").on(
	      'label -> task.label,
	      'task_user -> task.task_user,
	      'end_date -> task.end_date
	    ).executeUpdate()
	  }
	}

	

	def delete(id: Long): Int= {
	  DB.withConnection { implicit c =>
	     SQL("delete from task where id = {id}").on('id -> id).executeUpdate()	        
	  } match {
	  		case int => return(int)
	  }
	  
	}
}