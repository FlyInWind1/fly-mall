plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    //kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    //fly-spring
    implementation(project(":fly-spring:common"))
    implementation(project(":fly-spring:redis"))
    implementation(project(":fly-spring:security"))
    implementation(project(":fly-spring:elasticsearch"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

//    val ap=allprojects
//    println(ap)
//    println(project.allprojects)
    //database
    implementation("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")
//    implementation("mybatis-plus:mybatis-plus-boot-starter")
    implementation("org.postgresql:postgresql:${V.postgresql}")

    //spring附加包
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //工具
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${V.reactorKotlinExtensions}")
    //工具
    implementation("org.apache.commons:commons-lang3:${V.commonsLang3}")
    implementation("org.apache.commons:commons-collections4:${V.commonsCollections4}")
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
    implementation("org.mapstruct:mapstruct:${V.mapstruct}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${V.mapstruct}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
