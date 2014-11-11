import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
 
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.matcher._

import java.text.SimpleDateFormat
 
import models.Task
 
class ControllersTests extends Specification with JsonMatchers {
 
  "Controllers" should {
 
    "devolver una tarea en formato JSON con un GET /tasks/<id>" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
 
            val taskId = Task.create("prueba","anon")
            val Some(resultTask) = route(FakeRequest(GET, "/tasks/"+taskId))
            val resultJson: JsValue = contentAsJson(resultTask)         
            val resultString = Json.stringify(resultJson) 
            
 
            status(resultTask) must equalTo(OK)
            contentType(resultTask) must beSome.which(_ == "application/json")

            //   /#(index) accede al json 'index' del array (0-based)
            resultString must /#(0) /("id" -> taskId)
            resultString must /#(0) /("label" -> "prueba")
            resultString must /#(0) /("task_user" -> "anon")   
         }
      }

      "Tareas de un User en formato JSON con un GET /<login>/tasks " in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

            val usuario = "pepito"
            for(i <- 0 to 2)//creamos 3 tareas, pepito no tiene ninguna tarea incialmente
              Task.create("prueba "+ i, usuario)

            val listaTareas = route(FakeRequest(GET,"/"+ usuario+"/tasks")).get
            val resultJson = contentAsJson(listaTareas)         
            val resultString = Json.stringify(resultJson) 
            
            resultJson match {  
              case arr: JsArray => arr.value.length === 3 //comprobamos que sean 3 el length del js
              case _ => throw new Exception("json returned must be an array but it isn't")
            }
 
            status(listaTareas) must equalTo(OK)
            contentType(listaTareas) must beSome.which(_ == "application/json")

            //   /#(index) accede al json 'index' del array (0-based)            
            resultString must /#(0) /("label" -> "prueba 0")   
            resultString must /#(1) /("label" -> "prueba 1")   
            resultString must /#(2) /("label" -> "prueba 2")
         }
      }

      "devolver todas las tareas sin finalizar a partir de una fecha (dd-MM-yyyy) y de un usuario en JSON con un GET /<login>/<fecha>/tasks " in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
 
            val usuario = "madslover"
            val fechaParse1 = new SimpleDateFormat("dd-MM-yyyy").parse("13-06-1990");
            val fechaParse2 = new SimpleDateFormat("dd-MM-yyyy").parse("17-06-1990");
            for(i <- 0 to 4)//creamos 5 tareas, madslover no tiene ninguna tarea incialmente
              Task.create("pruebafecha "+ i, usuario, Option(fechaParse1)) //Tarea: "prueba + i, madslover, 13-06-1990"

            val listaTareas = route(FakeRequest(GET,"/"+ usuario+"/13-06-1989"+"/tasks")).get //hacemos la peticion poniendo como fecha un año antes de las introducidas
            val resultJson = contentAsJson(listaTareas)         
            val resultString = Json.stringify(resultJson) 

            resultJson match {  
              case arr: JsArray => arr.value.length === 5 //comprobamos que sean 5 el length del js
              case _ => throw new Exception("json returned must be an array but it isn't")
            }

            //   /#(index) accede al json 'index' del array (0-based)            
            resultString must /#(0) /("label" -> "pruebafecha 0")   
            resultString must /#(1) /("label" -> "pruebafecha 1")   
            resultString must /#(2) /("label" -> "pruebafecha 2")   
            resultString must /#(3) /("label" -> "pruebafecha 3")   
            resultString must /#(4) /("label" -> "pruebafecha 4")  

            Task.create("pruebafecha 5", usuario, Option(fechaParse2)) //Introducimos una tarea con fecha superior a las anteriores

            val listaTareas2 = route(FakeRequest(GET,"/"+ usuario+"/15-06-1990"+"/tasks")).get //hacemos la peticion en una fecha entre las introducidas antes y la nueva
            val resultJson2 = contentAsJson(listaTareas2)         
            val resultString2 = Json.stringify(resultJson2) 

            resultJson2 match {  
              case arr: JsArray => arr.value.length === 1 //comprobamos que sea 1
              case _ => throw new Exception("json returned must be an array but it isn't")
            }

            resultString2 must /#(0) /("label" -> "pruebafecha 5")   
         }
      }

      "devolver todas las tareas sin finalizar a partir de hoy(NOW) y de un usuario en JSON con un GET /<login>/tasks/now " in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
 
            val usuario = "madslover"
            val fechaParse1 = new SimpleDateFormat("dd-MM-yyyy").parse("12-11-2014");
            val fechaParse2 = new SimpleDateFormat("dd-MM-yyyy").parse("10-11-2014");
            for(i <- 0 to 2)//creamos 3 tareas, madslover no tiene ninguna tarea incialmente
              Task.create("pruebafecha "+ i, usuario, Option(fechaParse1)) //Tarea: "prueba + i, madslover, 12-11-2014"

            val listaTareas = route(FakeRequest(GET,"/"+ usuario+"/tasks/now")).get //hacemos la peticion
            val resultJson = contentAsJson(listaTareas)         
            val resultString = Json.stringify(resultJson) 

            resultJson match {  
              case arr: JsArray => arr.value.length === 3 //comprobamos que sean 5 el length del js
              case _ => throw new Exception("json returned must be an array but it isn't")
            }

            //   /#(index) accede al json 'index' del array (0-based)            
            resultString must /#(0) /("label" -> "pruebafecha 0")   
            resultString must /#(1) /("label" -> "pruebafecha 1")   
            resultString must /#(2) /("label" -> "pruebafecha 2")

            Task.create("pruebafecha 3", usuario, Option(fechaParse2)) //Introducimos una tarea con fecha anterior a hoy, en total hay 4 tareas a nombre de madslover

            val listaTareas2 = route(FakeRequest(GET,"/"+ usuario+"/tasks/now")).get //hacemos la peticion en una fecha entre las introducidas antes y la nueva
            val resultJson2 = contentAsJson(listaTareas2)         
            val resultString2 = Json.stringify(resultJson2) 

            resultJson2 match {  
              case arr: JsArray => arr.value.length === 3 //comprobamos que sea 3 y no 4, puesto que la tarea introducida tiene como campo fecha un dia anterior a NOW
              case _ => throw new Exception("json returned must be an array but it isn't")
            }

            resultString2 must /#(0) /("label" -> "pruebafecha 0")   
            resultString2 must /#(1) /("label" -> "pruebafecha 1")   
            resultString2 must /#(2) /("label" -> "pruebafecha 2")  
         }
      }

      "Create TAREA by POST /<user>/tasks" in {  
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val usuario = "pepito"
        val Some(result) = route(  
          FakeRequest(POST, "/"+usuario+"/tasks").withFormUrlEncodedBody(("label","esto es una prueba de post"),("task_user",usuario))  
          )

        status(result) must equalTo(CREATED)  

        val usuario2 = "pepitogrillo"
        val Some(result2) = route(  
          FakeRequest(POST, "/"+usuario2+"/tasks").withFormUrlEncodedBody(("label","esto es una prueba de post"),("task_user",usuario2))  
          )

        status(result2) must equalTo(BAD_REQUEST) //usuario no existe en base de datos  


        val Some(result3) = route(  
          FakeRequest(POST, "/"+usuario+"/tasks").withFormUrlEncodedBody(("label","esto es una prueba de post"))  
          )

        status(result3) must equalTo(BAD_REQUEST) //no se introduce usuario en el formulario (pero la url es /<user>/tasks)  
        }
      }
      


  }
}