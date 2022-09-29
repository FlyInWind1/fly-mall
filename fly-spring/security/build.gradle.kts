plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1"

dependencies {
    implementation(project(":fly-spring:common-core"))

    api("org.springframework.boot:spring-boot-starter-security:${V.springBoot}")
    api("org.springframework.boot:spring-boot-starter-json:${V.springBoot}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${V.springBoot}")

    api("io.jsonwebtoken:jjwt-api:${V.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${V.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${V.jjwt}")

    implementation("org.apache.commons:commons-lang3")
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
