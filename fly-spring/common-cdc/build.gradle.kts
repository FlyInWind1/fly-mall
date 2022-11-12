plugins {
    kotlin("jvm")
    id("io.freefair.lombok")

    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":fly-spring:common-core"))

    api("io.debezium:debezium-embedded:${V.debezium}")
    api("io.debezium:debezium-connector-postgres:${V.debezium}")
    api("io.debezium:debezium-storage-redis:2.1.0.Alpha1")
    api("org.springframework.boot:spring-boot-starter")

    compileOnly("com.baomidou:mybatis-plus-extension:${V.mybatisPlus}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
