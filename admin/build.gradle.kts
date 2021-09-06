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
    implementation(project(":fly-spring:common"))
    implementation(project(":fly-spring:redis"))
    implementation(project(":fly-spring:security"))
//    implementation(project(":fly-spring:elasticsearch"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    // spring cloud alibaba
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:${V.springCloudAlibaba}")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:${V.springCloudAlibaba}")

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
    implementation("org.mapstruct:mapstruct:${V.mapstruct}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${V.mapstruct}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
