rootProject.name = "fly-mall"
include("fly-spring:common")
include("fly-spring:redis")
include("fly-spring:security")
include("fly-spring:protosuff")
include("fly-spring:exception")
include("fly-spring:elasticsearch")
include("search")
include("admin")

//includeBuild("mybatis-plus"){
//    dependencySubstitution{
//        substitute(module("mybatis-plus:mybatis-plus-boot-starter")).with(project(":mybatis-plus-boot-starter"))
//    }
//}