plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
    id("io.freefair.lombok") version V.lombokPlugin

    kotlin("plugin.spring")
}

group = "flyinwind"

dependencies {
    api(project(":fly-spring:common-redis"))

    api("com.pig4cloud.plugin:multilevel-cache-spring-boot-starter:${V.multiLevelCache}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
