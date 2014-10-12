package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Option[Long] = None, label: String, task_user: String)



object Task{
		val task = {
	  get[Option[Long]]("id") ~ 
	  get[String]("label") ~
 	  get[String]("task_user") map {
	    case id~label~task_user => Task(id, label,task_user)
	  }
	}
	
	def all(): List[Task] = DB.withConnection { implicit c =>
	  SQL("select * from task").as(task *)
	}

	def getById(id: Long) = DB.withConnection{ implicit c=>
		SQL("select * from task where id = {id}").on('id -> id).as(task *)
	}

	def create(task: Task) {
	  DB.withConnection { implicit c =>
	    SQL("insert into task (label,task_user) values ({label}, {task_user})").on(
	      'label -> task.label,
	      'task_user -> task.task_user
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