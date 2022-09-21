plugins {
    `maven-publish`

    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "flyinwind"
version = "0.0.1"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":fly-spring:common-core"))
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive:${V.springBoot}")
    api("org.springframework.boot:spring-boot-starter-json:${V.springBoot}")
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
