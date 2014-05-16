package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.http.ContentTypes.JSON
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

/**
 * Specs2 tests
 */
@RunWith(classOf[JUnitRunner])
class MessageControllerSpec extends Specification {
  
  "MessageController" should {
    
    "getMessage should return JSON" in new WithApplication {
      val result = controllers.MessageController.getMessage(FakeRequest())

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
      contentAsString(result) must contain("Hello from Scala")
    }

  }
}