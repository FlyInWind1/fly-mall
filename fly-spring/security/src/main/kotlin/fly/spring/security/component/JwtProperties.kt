package fly.spring.security.component

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("security.jwt")
class JwtProperties {

    var tokenHeader: String = "token"

}