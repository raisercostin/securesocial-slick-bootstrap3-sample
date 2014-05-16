import play.api.mvc.{SimpleResult, RequestHeader, Filter}
import play.api.Logger
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object LoggingFilter extends Filter {
  def apply(nextFilter: (RequestHeader) => Future[SimpleResult]
             )(requestHeader: RequestHeader): Future[SimpleResult] = {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      import play.api.Routes
      val action = requestHeader.tags(play.api.Routes.ROUTE_CONTROLLER) +
      	"." + requestHeader.tags(Routes.ROUTE_ACTION_METHOD) +
        "-" + requestHeader.uri
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      Logger.info(s"${action} took ${requestTime}ms" +
        s" and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}