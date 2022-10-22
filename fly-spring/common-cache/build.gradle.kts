plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
    id("io.freefair.lombok") version V.lombokPlugin

    kotlin("plugin.spring")
}

group = "flyinwind"

dependencies {
    api(project(":fly-spring:common-redis"))

    // NullValueConfiguration 依赖
    compileOnly("org.springframework:spring-web")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")

    // multilevel-cache-spring-boot-starter 依赖
    api("com.github.ben-manes.caffeine:caffeine")
    compileOnly("org.springframework.boot:spring-boot-actuator")
    compileOnly("io.micrometer:micrometer-core")
    compileOnly("net.dreamlu:mica-auto:2.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
