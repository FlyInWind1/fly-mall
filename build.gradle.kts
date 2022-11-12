import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/public")
    }
}

plugins {
    kotlin("jvm") version V.kotlin
    kotlin("plugin.lombok") version V.kotlin
    kotlin("plugin.spring") version V.kotlin
    kotlin("kapt") version V.kotlin
    id("io.freefair.lombok") version V.lombokPlugin

    id("io.spring.dependency-management") version V.dependencyManagement
    // not apply in root project otherwise it will error can not find spring main class when :build
    id("org.springframework.boot") version V.springBoot apply false
}

allprojects {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        mavenLocal()
        mavenCentral()
    }

    // manage dependencies version by bom
    afterEvaluate {
        if (plugins.hasPlugin(DependencyManagementPlugin::class)) {
            dependencyManagement {
                imports {
                    mavenBom("org.springframework.boot:spring-boot-parent:${V.springBoot}")
                    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${V.springCloudDependencies}")
                }
            }
        }
    }

    if (plugins.hasPlugin(org.jetbrains.kotlin.lombok.gradle.LombokSubplugin::class)) {
        kotlinLombok {
            // https://kotlinlang.org/docs/lombok.html#using-the-lombok-configuration-file
            lombokConfigurationFile(file("lombok.config"))
        }
    }

    if (plugins.hasPlugin(Kapt3GradleSubplugin::class)) {
        kapt {
            // https://kotlinlang.org/docs/lombok.html#using-with-kapt
            keepJavacAnnotationProcessors = true
        }
    }

    configurations.all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"
