
import play.api.mvc._
import controllers._
//import play.api.filters.gzip.GzipFilter

object Global extends WithFilters(LoggingFilter) {
  // ...
}