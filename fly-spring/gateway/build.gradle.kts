plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "flyinwind"
version = "0.0.1"

dependencyManagement {
    imports{
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${V.springCloud}")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:${V.springCloudAlibaba}")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:${V.springCloudAlibaba}")
}