plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    //fly-spring
    implementation(project(":fly-spring:common-core"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    //database
    implementation("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")
    implementation("org.postgresql:postgresql")

    //spring附加包
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //工具
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${V.reactorKotlinExtensions}")
    //工具
    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.commons:commons-collections4:${V.commonsCollection4}")
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")


}

tasks.withType<Test> {
    useJUnitPlatform()
}
