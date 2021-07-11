import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/gradle-plugin/")
    }
}

plugins {
    kotlin("jvm") version V.kotlin
    kotlin("plugin.spring") version V.kotlin
    kotlin("kapt") version V.kotlin

    id("io.spring.dependency-management") version V.dependencyManagement
    // not apply in root project otherwise it will error can not find spring main class when :build
    id("org.springframework.boot") version V.springBoot apply false
}

allprojects {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
    }

    configurations.all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "16"
        }
    }
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"
