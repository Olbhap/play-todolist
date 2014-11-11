package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.text.SimpleDateFormat
import java.util.{Date}

case class Categoria(id: Option[Long] = None, nombre: String, task_user: String)

object Categoria{
      val categoria = {
     get[Option[Long]]("id") ~ 
     get[String]("nombre") ~
     get[String]("task_user")map {
       case id~nombre~task_user => Categoria(id, nombre,task_user)
     }
   }

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

   def getById(id: Long) = DB.withConnection{ implicit c=>
      SQL("select * from categoria where id = {id}").on('id -> id).as(categoria *)
   }

   def delete(id: Long): Int= {
     DB.withConnection { implicit c =>
        SQL("delete from categoria where id = {id}").on('id -> id).executeUpdate()         
     } match {
         case int => return(int)
     }     
   }
}