import java.time.Duration

plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.4"
}

version = "0.0.1-SNAPSHOT"

java {

}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}

repositories {
    mavenCentral()
}

extra["archunitVersion"] = "1.2.1"
extra["junitVersion"] = "5.10.2"
extra["springDataBomVersion"] = "2024.0.1"
extra["springCloudBomVersion"] = "2023.0.3"

dependencies {
//    <!-- Spring Boot Starter -->
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.data:spring-data-bom:${property("springDataBomVersion")}")
    }
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
    group = "verification"
    useJUnitPlatform {
        includeTags("medium")
    }
    timeout.set(Duration.ofSeconds(300))
    shouldRunAfter("test")
}
val largeTest = tasks.register("largeTest", Test::class.java) {
    group = "verification"
    useJUnitPlatform {
        includeTags("large")
    }
    timeout.set(Duration.ofHours(1))
    shouldRunAfter("mediumTest")
}
