plugins {
    `maven-publish`
    kotlin("jvm")

    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

group = "flyinwind"
version = "0.0.1"

dependencies {
    api(project(":fly-spring:common-core"))
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    api("org.springframework.boot:spring-boot-starter-json")
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
