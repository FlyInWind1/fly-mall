plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "flyinwind"
version = "0.0.1"

dependencies {
    api(kotlin("stdlib-jdk8"))
    implementation(project(":fly-spring:common"))

    api("org.springframework.boot:spring-boot-starter-security:${V.springBoot}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${V.springBoot}")

    api("io.jsonwebtoken:jjwt-api:${V.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${V.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${V.jjwt}")

    implementation("com.fasterxml.jackson.core:jackson-databind:${V.jackson}")

    implementation("org.apache.commons:commons-lang3:${V.commonsLang3}")
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}