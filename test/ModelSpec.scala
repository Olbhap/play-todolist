import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import java.util.{Date}

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ModelSpec extends Specification {
  
  import models._

  // -- Date helpers
  
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
  val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
  
  // --
  
  "Task model" should {
    
    "create and retrieve by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        //val fecha = new Date(2014,06,13);
        val id = Task.create("Creando tarea","pablogil");
        val task = Task.getById(id).head;
      
      
        task.label must equalTo("Creando tarea");
        task.task_user must equalTo("pablogil"); 
        
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