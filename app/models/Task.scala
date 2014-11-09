package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.text.SimpleDateFormat
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

	def getByUserCustomDate(login: String, fecha: String): List[Task] = 
	DB.withConnection { 
		val date_parse = new SimpleDateFormat("dd-MM-yyyy").parse(fecha)
		implicit c =>
	  SQL("select * from task where task_user = {login} and end_date > {date_parse}").on('login -> login, 'date_parse -> date_parse).as(task *)
	}

	def getByUserDate(login: String): List[Task] = 
	DB.withConnection { 
		implicit c =>
	  SQL("select * from task where task_user = {login} and end_date > NOW()").on('login -> login).as(task *)
	}

	def getById(id: Long) = DB.withConnection{ implicit c=>
		SQL("select * from task where id = {id}").on('id -> id).as(task *)
	}

	def create(label: String, task_user: String, end_date: Option[Date] = None): Long = {
      DB.withConnection { implicit c =>
         val id: Option[Long]  = 
            SQL("insert into task (label, task_user, end_date) values ({label}, {task_user},{end_date})").on(
               'label -> label,
               'task_user -> task_user,
               'end_date -> end_date
            ).executeInsert()

         //Devolvemos -1 si el insert devuelve None
         id.getOrElse(-1)
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