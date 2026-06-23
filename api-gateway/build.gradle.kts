import java.time.Duration

plugins {
    java
    id("org.springframework.boot") version "4.0.7"
    id("io.spring.dependency-management") version "1.1.7"
}

version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}

repositories {
    mavenCentral()
}

extra["junitVersion"] = "6.0.1"
extra["springCloudBomVersion"] = "2025.1.2"

dependencies {
//    <!-- Spring Boot Starter -->
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junitVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junitVersion")}")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
    }
}

tasks.test {
    useJUnitPlatform {
        excludeTags("medium", "large")
        timeout.set(Duration.ofSeconds(60))
    }
}

val mediumTest = tasks.register("mediumTest", Test::class.java) {
    description = "medium sized test"
    group = "verification"
    useJUnitPlatform {
        includeTags("medium")
    }
    timeout.set(Duration.ofMinutes(5))
    shouldRunAfter("test")
}
val largeTest = tasks.register("largeTest", Test::class.java) {
    description = "large sized test"
    group = "verification"
    useJUnitPlatform {
        includeTags("large")
    }
    timeout.set(Duration.ofHours(1))
    shouldRunAfter("mediumTest")
}
