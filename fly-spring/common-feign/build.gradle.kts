plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":fly-spring:common-core"))

    api("com.playtika.reactivefeign:feign-reactor-spring-cloud-starter:${V.springCloud}")
    // feign-reactor-spring-cloud-starter forget to add feign-slf4j as a dependency
    api("io.github.openfeign:feign-slf4j") {
        exclude("io.github.openfeign", "feign-core")
    }
    api("org.hibernate.validator:hibernate-validator")
}
