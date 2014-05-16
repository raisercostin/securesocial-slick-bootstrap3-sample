package controllers

import play.api.Routes
import play.api.libs.functional.syntax.toContraFunctorOps
import play.api.libs.json.Json
import play.api.mvc.Action

case class Message(value: String)

object MessageController extends securesocial.core.SecureSocial {
  implicit val fooWrites = Json.writes[Message]
  def ???!(msg: String): Nothing = throw new RuntimeException(msg)
  import play.api.Play.current
  def index() = UserAwareAction {
    implicit request =>
      val user = request.user
      Ok(views.html.index("hi user " + user,"")(request))
  }
  def getMessage = Action {
    Ok(Json.toJson(Message("Hello from Scala")))
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(Routes.javascriptRouter("jsRoutes")(
      securesocial.controllers.routes.javascript.LoginPage.login)).as(JAVASCRIPT)
  }

  def search = index()
  //  UserAwareAction {
  //    implicit request =>
  //      val query = request.getQueryString("query")
  //      
  //  }
  def search2(query: String) = index()
}