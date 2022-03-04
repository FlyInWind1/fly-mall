package fly.spring.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import fly.spring.security.component.JwtAuthenticationTokenFilter
import fly.spring.security.component.JwtProperties
import fly.spring.security.component.RestAccessDeniedHandler
import fly.spring.security.util.JwtUtil
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 * Security config
 *
 * @constructor Create empty Security config
 */
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig {
    /**
     * Security filter chain
     *
     * @param http
     * @return
     */
    @Bean
    fun securityFilterChain(http: ServerHttpSecurity,
                            objectMapper: ObjectMapper,
                            jwtUtil: JwtUtil,
                            userDetailsService: UserDetailsService,
                            jwtProperties: JwtProperties
    ): SecurityWebFilterChain {
        return http
                .csrf {
                    //disable csrf for jwt
                    it.disable()
                }
                .headers {
                    //disable cache
                    it.cache()
                }
                .authorizeExchange {
                    it.pathMatchers("/login")
                            .permitAll()

                            .pathMatchers(HttpMethod.GET, "/static")
                            .permitAll()

                            .pathMatchers(HttpMethod.OPTIONS)
                            .permitAll()

                            .anyExchange()
//                            .authenticated()
                            .permitAll()
                }
                .formLogin {
                    it.loginPage("/login")
                }
                .addFilterBefore(JwtAuthenticationTokenFilter(jwtUtil, userDetailsService, jwtProperties), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling {
                    it.accessDeniedHandler(RestAccessDeniedHandler(objectMapper))
//                            .authenticationEntryPoint()
                }
                .build()


//                .authorizeExchange {
//                    auth
//                }

//       return http{
//            authorizeExchange {
//                authorize("/log-in", permitAll)
//                authorize("/", permitAll)
//                authorize("/css/**", permitAll)
//                authorize("/user/**", hasAuthority("ROLE_USER"))
//            }
//            formLogin {
//                loginPage = "/log-in"
//            }
//        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * User detail service
     *
     * @return
     */
//    @Bean
//    fun userDetailService(): MapReactiveUserDetailsService {
//        val user = User.builder()
//                .username("fly")
//                .password("password")
//                .roles("USER")
//                .build()
//        return MapReactiveUserDetailsService(user)
//    }

//    @Bean
//    fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }


    /**
     * User detail service
     *
     * @return
     */
//    fun userDetailService(): UserDetailsService {
//        return UserDetailsService { username ->
//
//        }
//    }
}