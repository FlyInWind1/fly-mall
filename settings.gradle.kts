pluginManagement {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/public")
    }
}

rootProject.name = "fly-mall"

include("fly-spring:common-core")
include("fly-spring:common-feign")
include("fly-spring:redis")
include("fly-spring:security")
include("fly-spring:protosuff")
include("fly-spring:exception")
include("fly-spring:elasticsearch")
include("fly-spring:gateway")
include("search")
include("admin")
include("oauth2:oauth2-server")
include("oauth2:login")
