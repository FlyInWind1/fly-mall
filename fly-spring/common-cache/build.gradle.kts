plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
    id("io.freefair.lombok")

    kotlin("plugin.spring")
}

group = "flyinwind"

dependencies {
    api(project(":fly-spring:common-redis"))

    api("com.pig4cloud.plugin:multilevel-cache-spring-boot-starter:3.0.0")

    // multilevel-cache-spring-boot-starter 依赖
    compileOnly("org.springframework.boot:spring-boot-actuator")
    compileOnly("io.micrometer:micrometer-core")
    compileOnly("net.dreamlu:mica-auto:2.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
