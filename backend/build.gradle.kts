import java.time.Duration

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.15.2")
    }
}
plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.flywaydb.flyway") version "10.15.2"
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

extra["springModulithVersion"] = "1.2.1"
extra["springModulithInsightVersion"] = "1.2.3"
extra["guavaVersion"] = "33.3.0-jre"
extra["icu4jVersion"] = "74.2"
extra["yaviVersion"] = "0.14.1"
extra["jiltVersion"] = "1.6.1"
extra["jdbcPostgresqlVersion"] = "10.15.2"
extra["openapiUiVersion"] = "2.3.0"
extra["jmoleculesBomVersion"] = "2023.1.2"
extra["archunitVersion"] = "1.2.1"
extra["junitVersion"] = "5.10.3"
extra["springDataBomVersion"] = "2024.0.1"
extra["springCloudBomVersion"] = "2023.0.3"
extra["spotbugsAnnotationVersion"] = "4.8.4"
extra["libphonenumberVersion"] = "8.13.43"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:${property("jdbcPostgresqlVersion")}")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-starter-jpa")
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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("openapiUiVersion")}")
    testImplementation("org.jmolecules.integrations:jmolecules-archunit")
    testImplementation("com.tngtech.archunit:archunit-junit5:${property("archunitVersion")}")
    testImplementation("com.github.spotbugs:spotbugs-annotations:${property("spotbugsAnnotationVersion")}")
    runtimeOnly("org.springframework.modulith:spring-modulith-starter-insight:${property("springModulithInsightVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junitVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junitVersion")}")
    implementation("com.google.guava:guava:${property("guavaVersion")}")

    implementation("com.ibm.icu:icu4j:${property("icu4jVersion")}")
    testRuntimeOnly("com.h2database:h2")
    // https://mvnrepository.com/artifact/am.ik.yavi/yavi

    implementation("am.ik.yavi:yavi:${property("yaviVersion")}")
    annotationProcessor("cc.jilt:jilt:${property("jiltVersion")}")
    //    implementation("olg.zalando:logbook-spring-boot-starter:3.9.0")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    // https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber
    implementation("com.googlecode.libphonenumber:libphonenumber:${property("libphonenumberVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
    imports {
        mavenBom("org.jmolecules:jmolecules-bom:${property("jmoleculesBomVersion")}")
    }
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
