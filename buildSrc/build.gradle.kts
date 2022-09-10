buildscript {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/public")
    }
}

plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
}
