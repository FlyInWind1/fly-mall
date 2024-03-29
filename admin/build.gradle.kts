plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

//dependencyManagement {
//    imports {
//        mavenBom("io.r2dbc:r2dbc-bom:Arabba-SR12")
//    }
//}

dependencies {
    //fly-spring
    implementation(project(":fly-spring:common-core"))
    implementation(project(":fly-spring:common-cache"))
    implementation(project(":fly-spring:common-redis"))
    implementation(project(":fly-spring:security"))
    implementation(project(":fly-spring:common-feign"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

//    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    // spring cloud alibaba
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:${V.springCloudAlibaba}")
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:${V.springCloudAlibaba}")

    //database
    implementation("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")
    implementation("org.postgresql:postgresql")

//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//    implementation("io.r2dbc:r2dbc-postgresql")

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

    implementation("io.projectreactor:reactor-tools")

    implementation("net.bytebuddy:byte-buddy-dep:${V.byteBuddy}")

    testImplementation(testFixtures(project(":fly-spring:common-core")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
    jvmArgs(
        "--add-opens",
        "java.base/java.util.concurrent=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.lang.invoke=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED"
    )
}
