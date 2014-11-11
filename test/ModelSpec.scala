import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import java.util.{Date}
import java.text.SimpleDateFormat

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ModelSpec extends Specification {
  
  import models._

  // -- Date helpers
  
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("dd-MM-yyyy").format(date) == str
  
  // --
  
  "Task model" should {
    
    "create without DATE and retrieve by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        //val fecha = new Date(2014,06,13);
        val id = Task.create("Creando tarea","pablogil");
        val task = Task.getById(id).head; //getById devuelve una lista      
      
        task.label must equalTo("Creando tarea");
        task.task_user must equalTo("pablogil"); 
        
      }
    }

    "create with DATE and retrieve by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val date_parse = new SimpleDateFormat("dd-MM-yyyy").parse("13-06-1990");
        val id = Task.create("Cumpleaños","pablogil",Option(date_parse));
        val task = Task.getById(id).head; //getById devuelve una lista     
        
      
        task.label must equalTo("Cumpleaños");
        task.task_user must equalTo("pablogil"); 
        task.end_date must beSome.which(dateIs(_, "13-06-1990"))  
        
      }
    }
    
    "delete if exists" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val id = Task.create("tarea a borrar","pablogil");
        val task = Task.getById(id).head;

        Task.delete(id);
        val recuperar = Task.getById(id);
        recuperar must have length(0); //si se intenta recuperar, se recuperará una lista vacia
      }
    }


     "retrieve by custom date" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          val tasks = Task.getByUserCustomDate("pablogil","11-10-2013");

          //Según nuestra base de datos, deberá aparecer 2 tareas
          tasks must have length(2);
        }
      }


      "retrieve by user" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          val tasks = Task.getByUser("domingogallardo");

          //Según nuestra base de datos, deberá aparecer 4 tareas
          tasks must have length(4);
        }
      }


      "retrieve by login NOW" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          val tasks = Task.getByUserDate("pablogil");

          //Según nuestra base de datos, deberá aparecer 1 tareas
          tasks must have length(1);
        }
      }
    

    
    /*"be listed along its companies" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val computers = Computer.list()

        computers.total must equalTo(574)
        computers.items must have length(10)

      }
    }
    
    "be updated if needed" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Computer.update(21, Computer(name="The Macintosh", introduced=None, discontinued=None, companyId=Some(1)))
        
        val Some(macintosh) = Computer.findById(21)
        
        macintosh.name must equalTo("The Macintosh")
        macintosh.introduced must beNone
        
      }
    }
    */
  }

  "Categoria model" should {
    "retrieve by categoria 1" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          val tasks = Task.getByUserCat("pablogil", 1);

          //Según nuestra base de datos, deberá aparecer 2 tareas
          tasks must have length(2);
        }
      }

      "retrieve by categoria 2" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          val tasks = Task.getByUserCat("domingogallardo", 2);

          //Según nuestra base de datos, deberá aparecer 1 tarea
          tasks must have length(1);
        }
      }

      "create and retrieve by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        //val fecha = new Date(2014,06,13);
        val id = Categoria.create("Categoria de pruebas","pablogil");
        val cat = Categoria.getById(id).head; //getById devuelve una lista      
      
        cat.nombre must equalTo("Categoria de pruebas");
        cat.task_user must equalTo("pablogil"); 
        
      }
    }

    "delete if exists" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val id = Categoria.create("Categoria de pruebas","pablogil");
        val cat = Categoria.getById(id).head;

        Categoria.delete(id);
        val recuperar = Categoria.getById(id);
        recuperar must have length(0); //si se intenta recuperar, se recuperará una lista vacia
      }
    }

  }
  
}