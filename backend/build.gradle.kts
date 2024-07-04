import java.time.Duration

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.4"
}

version = "0.0.1-SNAPSHOT"

java {

}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "1.2.0"
extra["jmoleculesBomVersion"] = "2023.1.2"
extra["archunitVersion"] = "1.2.1"
extra["junitVersion"] = "5.10.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.14.0")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-starter-jdbc")
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
    runtimeOnly("org.springframework.modulith:spring-modulith-observability")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    implementation("org.jmolecules:jmolecules-layered-architecture")
    implementation("org.jmolecules:jmolecules-onion-architecture")
    implementation("org.jmolecules:jmolecules-ddd")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    testImplementation("org.jmolecules.integrations:jmolecules-archunit")
    testImplementation("com.tngtech.archunit:archunit-junit5:${property("archunitVersion")}")
    testImplementation("com.github.spotbugs:spotbugs-annotations:4.8.4")
    runtimeOnly("org.springframework.modulith:spring-modulith-starter-insight:1.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junitVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junitVersion")}")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("com.ibm.icu:icu4j:74.2")
    runtimeOnly("com.h2database:h2")
// https://mvnrepository.com/artifact/am.ik.yavi/yavi
    implementation("am.ik.yavi:yavi:0.14.1")
    annotationProcessor("cc.jilt:jilt:1.6.1")
    implementation("olg.zalando:logbook-spring-boot-starter:3.9.0")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
    imports {
        mavenBom("org.jmolecules:jmolecules-bom:${property("jmoleculesBomVersion")}")
    }
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}

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