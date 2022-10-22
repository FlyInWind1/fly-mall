plugins {
    `maven-publish`

    kotlin("jvm")
    kotlin("plugin.spring")

    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1-SNAPSHOT"

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-elasticsearch")
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
