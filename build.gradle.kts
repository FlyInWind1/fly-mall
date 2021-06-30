import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven("https://maven.aliyun.com/nexus/content/repositories/gradle-plugin")
        gradlePluginPortal()
        mavenCentral()
    }
//    dependencies {
//        classpath("se.patrikerdes:gradle-use-latest-versions-plugin:+")
//        classpath("com.github.ben-manes:gradle-versions-plugin:+")
//    }
}

plugins {
    kotlin("jvm") version V.kotlin
    kotlin("plugin.spring") version V.kotlin
    kotlin("kapt") version V.kotlin

    id("io.spring.dependency-management") version V.dependencyManagement
    id("org.springframework.boot") version V.springBoot
}

allprojects {
//apply(plugin = "com.github.ben-manes.versions")
//    apply {
//        plugin("se.patrikerdes.use-latest-versions")
//        plugin("com.github.ben-manes.versions")
//    }

    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
    }

//    afterEvaluate {
//        configurations.compileOnly {
//            extendsFrom(configurations.annotationProcessor.get())
//        }
//    }

    configurations.all {
        exclude("org.junit.vintage", "junit-vintage-engine")
        exclude("org.springframework.boot", "spring-boot-starter-logging")
        //myBatis plus 在依赖fastjson
        exclude("com.alibaba", "fastjson")
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
