import cats.effect.{ContextShift, IO, Resource}
import config.DataBaseConfig
import doobie.hikari._
import modules.SecurityModule
import org.flywaydb.play.FlywayPlayComponents
import org.pac4j.core.config.Config
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.{CallbackController, LogoutController}
import play.api._
import play.api.http.{DefaultFileMimeTypesProvider, FileMimeTypes}
import play.api.i18n._
import play.api.mvc.BodyParsers.Default
import play.api.mvc.{BodyParsers, ControllerComponents}
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
    with SecurityComponents
    with play.filters.HttpFiltersComponents
    with _root_.controllers.AssetsComponents
    with FlywayPlayComponents
    with I18nComponents
{
  flywayPlayInitializer
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  lazy val dataBaseConfig: DataBaseConfig = loadConfigOrThrow[_root_.config.DataBaseConfig]("db.default")
  lazy val transactor: Resource[IO, HikariTransactor[IO]] = _root_.config.DataBaseConfig.dbTransactor[IO](dataBaseConfig)

  lazy val callbackController = new CallbackController()
  callbackController.setDefaultUrl("/?defaulturlafterlogout")
  callbackController.setMultiProfile(true)
  // logout
  lazy  val logoutController = new LogoutController()
  logoutController.setDefaultUrl("/")
  lazy val userRepoInterpreter = _root_.repository.doobie.UserRepositoryInterpreter(transactor)
  lazy val userService = _root_.domian.user.UserService(userRepoInterpreter)
  lazy val securityComponents = new DefaultSecurityComponents(playSessionStore, config, parser, components);

  lazy val homeController = new _root_.controllers.HomeController(securityComponents, userService = userService)
  lazy val router: Router = new _root_.router.Routes(
    httpErrorHandler,
    homeController,
    callbackController,
    logoutController,
    assets
  )
  val sKey = configuration.get[String]("play.http.secret.key").substring(0, 16)
  val dataEncrypter = new _root_.org.pac4j.play.store.ShiroAesDataEncrypter(sKey)
  override lazy val playSessionStore = new _root_.org.pac4j.play.store.PlayCookieSessionStore(dataEncrypter)
  override lazy val components: ControllerComponents = controllerComponents
  override lazy val config: Config = new SecurityModule(environment,configuration).provideConfig()
  override lazy val executionContext: ExecutionContext =  actorSystem.dispatcher
  override lazy val langs: Langs = new DefaultLangsProvider(configuration).get
  override lazy val fileMimeTypes: FileMimeTypes =new DefaultFileMimeTypesProvider(httpConfiguration.fileMimeTypes).get
  override lazy val messagesApi: MessagesApi = new DefaultMessagesApiProvider(environment, configuration, langs, httpConfiguration).get
  override lazy val parser: BodyParsers.Default = new Default(parsers)
}