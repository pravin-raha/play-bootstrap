package modules

import com.google.inject.{AbstractModule, Provides}
import controllers.{CustomAuthorizer, DemoHttpActionAdapter}
import org.pac4j.cas.client.CasClient
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.client.direct.AnonymousClient
import org.pac4j.core.config.Config
import org.pac4j.core.matching.PathMatcher
import org.pac4j.core.profile.CommonProfile
import org.pac4j.http.client.direct.{DirectBasicAuthClient, ParameterClient}
import org.pac4j.http.client.indirect.{FormClient, IndirectBasicAuthClient}
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
import org.pac4j.oauth.client.{FacebookClient, TwitterClient}
import org.pac4j.oidc.client.OidcClient
import org.pac4j.oidc.config.OidcConfiguration
import org.pac4j.oidc.profile.OidcProfile
import org.pac4j.play.scala.{DefaultSecurityComponents, Pac4jScalaTemplateHelper, SecurityComponents}
import org.pac4j.play.store.{PlayCookieSessionStore, PlaySessionStore, ShiroAesDataEncrypter}
import org.pac4j.play.{CallbackController, LogoutController}
import play.api.{Configuration, Environment}

class SecurityModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  val baseUrl: String = configuration.get[String]("baseUrl")

  override def configure(): Unit = {
    val sKey = configuration.get[String]("play.http.secret.key").substring(0, 16)
    val dataEncrypter = new ShiroAesDataEncrypter(sKey)
    val playSessionStore = new PlayCookieSessionStore(dataEncrypter)
    bind(classOf[PlaySessionStore]).toInstance(playSessionStore)


    bind(classOf[SecurityComponents]).to(classOf[DefaultSecurityComponents])

    bind(classOf[Pac4jScalaTemplateHelper[CommonProfile]])

    // callback
    val callbackController = new CallbackController()
    callbackController.setDefaultUrl("/?defaulturlafterlogout")
    callbackController.setMultiProfile(true)
    bind(classOf[CallbackController]).toInstance(callbackController)

    // logout
    val logoutController = new LogoutController()
    logoutController.setDefaultUrl("/")
//    logoutController.setDestroySession(true)
    bind(classOf[LogoutController]).toInstance(logoutController)
  }

  @Provides
  def provideFacebookClient: FacebookClient = {
    val fbId = configuration.getOptional[String]("fbId").get
    val fbSecret = configuration.getOptional[String]("fbSecret").get
    new FacebookClient(fbId, fbSecret)
  }

  @Provides
  def provideOidcClient: OidcClient[OidcProfile, OidcConfiguration] = {
    val oidcConfiguration = new OidcConfiguration()
    val key = configuration.getOptional[String]("oidcClientId").get
    val secret = configuration.getOptional[String]("oidcClientSecrect").get
    oidcConfiguration.setClientId(key)
    oidcConfiguration.setSecret(secret)
    oidcConfiguration.setLogoutUrl("/logout")
    oidcConfiguration.setDiscoveryURI("http://localhost:8080/auth/realms/demo/.well-known/openid-configuration")
    //    oidcConfiguration.addCustomParam("prompt", "consent")
    val oidcClient = new OidcClient[OidcProfile, OidcConfiguration](oidcConfiguration)
    //    oidcClient.addAuthorizationGenerator(new RoleAdminAuthGenerator)
    oidcClient
  }

  @Provides
  def provideDirectBasicAuthClient: DirectBasicAuthClient = new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator)

  @Provides
  def provideFormClient: FormClient = new FormClient(baseUrl + "/loginForm", new SimpleTestUsernamePasswordAuthenticator())

  @Provides
  def provideConfig(facebookClient: FacebookClient, twitterClient: TwitterClient, formClient: FormClient, indirectBasicAuthClient: IndirectBasicAuthClient,
                    casClient: CasClient, oidcClient: OidcClient[OidcProfile, OidcConfiguration], parameterClient: ParameterClient, directBasicAuthClient: DirectBasicAuthClient): Config = {
    val clients = new Clients(baseUrl + "/callback", provideFormClient, provideDirectBasicAuthClient,provideOidcClient)

    val config = new Config(clients)

    config.addAuthorizer("admin", new RequireAnyRoleAuthorizer[Nothing]("ROLE_ADMIN"))
    config.addAuthorizer("custom", new CustomAuthorizer)
    config.addMatcher("excludedPath", new PathMatcher().excludeRegex("^/facebook/notprotected\\.html$"))
    config.setHttpActionAdapter(new DemoHttpActionAdapter())
    config
  }
}