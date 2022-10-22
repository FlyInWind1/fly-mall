plugins {
    `java-test-fixtures`
    kotlin("jvm")
    id("io.freefair.lombok") version V.lombokPlugin

    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

group = "flyinwind"
version = "0.0.1"

dependencies {
    //kotlin
    api(kotlin("reflect"))

    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.boot:spring-boot-starter-log4j2")

//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${V.reactorKotlinExtensions}")

    api("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
//    annotationProcessor("org.projectlombok:lombok:${V.lombok}")

    // for CacheKeyGeneratorUtil
    api("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")

    api("cn.hutool:hutool-core:${V.hutool}")
    api("cn.hutool:hutool-extra:${V.hutool}")
    api("org.apache.commons:commons-collections4:${V.commonsCollection4}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")
    testImplementation(kotlin("test"))
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

// may be a bug https://youtrack.jetbrains.com/issue/KT-46165
//task processPackageResources (type: Copy) { duplicatesStrategy = 'include' }
tasks.withType(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
