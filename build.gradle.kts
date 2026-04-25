plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.spring") version "2.3.10"
    id("org.springframework.boot") version "4.1.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "al.bruno.walks"
version = "1.0.0"
description = "Spring Boot GraphQL Proxy/Cache for Contentful"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.10.Final:osx-aarch_64")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}