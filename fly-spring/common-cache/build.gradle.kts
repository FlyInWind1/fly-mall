plugins {
    kotlin("jvm")

    id("io.freefair.lombok") version V.lombokPlugin
}

group = "flyinwind"

dependencies {
    api(project(":fly-spring:common-core"))

    api("com.pig4cloud.plugin:multilevel-cache-spring-boot-starter:${V.multiLevelCache}")
}
