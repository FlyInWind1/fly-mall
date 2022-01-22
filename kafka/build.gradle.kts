plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")

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

    implementation("org.springframework.kafka:spring-kafka")


//    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    // spring cloud alibaba
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:${V.springCloudAlibaba}")
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:${V.springCloudAlibaba}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    //工具
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
