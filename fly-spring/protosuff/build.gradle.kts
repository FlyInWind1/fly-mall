plugins {
    kotlin("jvm")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(kotlin("stdlib-jdk8"))

//    implementation("com.google.protobuf:protobuf-java:3.13.0")
    implementation("io.protostuff:protostuff-runtime:1.7.2")
    implementation("io.protostuff:protostuff-core:1.7.2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:${V.springBoot}")
}
