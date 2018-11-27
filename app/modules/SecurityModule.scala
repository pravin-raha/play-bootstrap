package modules

import controllers.{CustomAuthorizer, DemoHttpActionAdapter}
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.core.matching.PathMatcher
import org.pac4j.oauth.client.FacebookClient
import org.pac4j.oidc.client.OidcClient
import org.pac4j.oidc.config.OidcConfiguration
import org.pac4j.oidc.profile.OidcProfile
import play.api.{Configuration, Environment}

class SecurityModule(environment: Environment, configuration: Configuration) {

  val baseUrl: String = configuration.get[String]("baseUrl")

  private def provideFacebookClient: FacebookClient
  = {
    val fbId = configuration.getOptional[String]("fbId").get
    val fbSecret = configuration.getOptional[String]("fbSecret").get
    new FacebookClient(fbId, fbSecret)
  }

  private def provideOidcClient: OidcClient[OidcProfile, OidcConfiguration] = {
    val oidcConfiguration = new OidcConfiguration()
    val key = configuration.getOptional[String]("googleKey").get
    val secret = configuration.getOptional[String]("googleSecret").get
    oidcConfiguration.setClientId(key)
    oidcConfiguration.setSecret(secret)
    oidcConfiguration.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration")
    //    oidcConfiguration.addCustomParam("prompt", "consent")
    val oidcClient = new OidcClient[OidcProfile, OidcConfiguration](oidcConfiguration)
    //    oidcClient.addAuthorizationGenerator(new RoleAdminAuthGenerator)
    oidcClient
  }

  def provideConfig(): Config = {
    val clients = new Clients(baseUrl + "/callback", provideFacebookClient)

    val config = new Config(clients)
    config.addAuthorizer("admin", new RequireAnyRoleAuthorizer[Nothing]("ROLE_ADMIN"))
    config.addAuthorizer("custom", new CustomAuthorizer)
    config.addMatcher("excludedPath", new PathMatcher().excludeRegex("^/facebook/notprotected\\.html$"))
    config.setHttpActionAdapter(new DemoHttpActionAdapter())
    config
  }
}