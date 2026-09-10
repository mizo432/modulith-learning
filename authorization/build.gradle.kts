import java.time.Duration

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
}

version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:unchecked")
    }
}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}

repositories {
    mavenCentral()
}

extra["guavaVersion"] = "33.7.1-jre"
extra["icu4jVersion"] = "76.1"
extra["jiltVersion"] = "1.7"
extra["jdbcPostgresqlVersion"] = "11.3.4"
extra["openapiUiVersion"] = "2.8.5"
extra["jmoleculesBomVersion"] = "2023.2.1"
extra["archunitVersion"] = "1.3.0"
extra["junitVersion"] = "6.1.3"
extra["springCloudBomVersion"] = "2025.0.0"
extra["spotbugsAnnotationVersion"] = "4.10.4"
extra["libphonenumberVersion"] = "9.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.spotbugs:spotbugs-annotations:${property("spotbugsAnnotationVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.google.guava:guava:${property("guavaVersion")}")
//    testRuntimeOnly("com.h2database:h2")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") {
        exclude("com.google.guava")
    }
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:7.1.1")
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).addStringOption("encoding", "UTF-8")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.1.1")
    }
}

jacoco {
    toolVersion = "0.8.15"
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
    timeout.set(Duration.ofMinutes(5))
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

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
