plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    kotlin("plugin.lombok")
    id("io.freefair.lombok")

    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":fly-spring:common-core"))

    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-json")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    api("io.jsonwebtoken:jjwt-api:${V.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${V.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${V.jjwt}")

    implementation("org.apache.commons:commons-lang3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
