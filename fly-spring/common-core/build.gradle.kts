plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.freefair.lombok") version V.lombokPlugin
}

group = "flyinwind"
version = "0.0.1"

dependencies {
    //kotlin
    api(kotlin("reflect"))

    api("org.springframework.boot:spring-boot-starter-webflux:${V.springBoot}")
    api("org.springframework.boot:spring-boot-starter-log4j2:${V.springBoot}")

//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${V.reactorKotlinExtensions}")

    api("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
//    annotationProcessor("org.projectlombok:lombok:${V.lombok}")

    compileOnly("com.baomidou:mybatis-plus-boot-starter:${V.mybatisPlus}")

    api("cn.hutool:hutool-core:${V.hutool}")
    api("cn.hutool:hutool-extra:${V.hutool}")

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
