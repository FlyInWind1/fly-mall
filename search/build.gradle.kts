plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(kotlin("reflect"))

    implementation(project(":fly-spring:common-core"))
//    implementation(project(":fly-spring:security"))
    implementation(project(":fly-spring:elasticsearch"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    //数据库
    implementation("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")
    implementation("org.postgresql:postgresql")

    //spring附加包
//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //工具
    implementation("org.apache.commons:commons-collections4:${V.commonsCollection4}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${V.reactorKotlinExtensions}")
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
