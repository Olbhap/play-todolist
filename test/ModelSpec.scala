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
        recuperar must have length(0);
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
  
}