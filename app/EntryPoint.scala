import cats.effect.{ContextShift, IO, Resource}
import config.DataBaseConfig
import doobie.hikari._
import org.flywaydb.play.FlywayPlayComponents
import play.api._
import play.api.routing.Router
import pureconfig.loadConfigOrThrow

import scala.concurrent.ExecutionContext

class EntryPoint extends ApplicationLoader {
  private var components: Components = _

  def load(context: ApplicationLoader.Context): Application = {
    components = new Components(context)
    components.application
  }
}

class Components(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context)
    with play.filters.HttpFiltersComponents
    with _root_.controllers.AssetsComponents
    with FlywayPlayComponents {
  flywayPlayInitializer
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val dataBaseConfig: DataBaseConfig = loadConfigOrThrow[_root_.config.DataBaseConfig]("db.default")
  val transactor: Resource[IO, HikariTransactor[IO]] = _root_.config.DataBaseConfig.dbTransactor[IO](dataBaseConfig)


  val userRepoInterpreter = _root_.repository.doobie.UserRepositoryInterpreter(transactor)
  val userService = _root_.domian.user.UserService(userRepoInterpreter)
  val homeController = new _root_.controllers.HomeController(controllerComponents, userService = userService)
  val router: Router = new _root_.router.Routes(httpErrorHandler, homeController, assets)

}