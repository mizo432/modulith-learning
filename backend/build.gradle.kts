import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.time.Duration

buildscript {
    repositories {
        mavenCentral() // Maven Centralリポジトリから依存関係を取得する
    }
    dependencies {
        // Flyway（データベース移行ツール）のクラスパス追加
        // classpath("org.flywaydb:flyway-database-postgresql:11.3.4")
    }
}
plugins {
    // Javaプラグインを適用（Javaプロジェクトのサポート）
    java
    // Spring Bootプラグイン
    id("org.springframework.boot") version "4.1.0"
    // Spring関連の依存関係の管理用プラグイン
    id("io.spring.dependency-management") version "1.1.7"
    id("se.patrikerdes.use-latest-versions") version "0.2.19"
    id("com.github.ben-manes.versions") version "0.53.0"
    // Flywayプラグイン（DBマイグレーション）
    // id("org.flywaydb.flyway") version "11.3.4"
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
        // コンパイラの警告を有効化（未チェック警告）
        options.compilerArgs.add("-Xlint:unchecked")
    }
}
configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "2.1.0"
extra["springModulithInsightVersion"] = "2.1.0"
extra["guavaVersion"] = "33.6.0-jre"
extra["icu4jVersion"] = "77.1"
extra["yaviVersion"] = "0.16.0"
extra["jiltVersion"] = "1.8.2"
extra["jdbcPostgresqlVersion"] = "11.3.4"
extra["openapiUiVersion"] = "2.8.9"
extra["jmoleculesBomVersion"] = "2023.2.1"
extra["archunitVersion"] = "1.3.0"
extra["junitVersion"] = "6.1.0"
extra["springCloudBomVersion"] = "2025.1.2"
extra["spotbugsAnnotationVersion"] = "4.9.3"
extra["libphonenumberVersion"] = "9.0.9"
extra["poiVersion"] = "5.5.0"
extra["jspecifyVersion"] = "1.0.0"
extra["assertjDbVersion"] = "3.0.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-starter-jpa")
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
    runtimeOnly("org.springframework.modulith:spring-modulith-observability")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    implementation("org.jmolecules:jmolecules-onion-architecture")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("openapiUiVersion")}")
    testImplementation("com.github.spotbugs:spotbugs-annotations:${property("spotbugsAnnotationVersion")}")
    runtimeOnly("org.springframework.modulith:spring-modulith-starter-insight:${property("springModulithInsightVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.google.guava:guava:${property("guavaVersion")}")
    implementation("org.jspecify:jspecify:${property("jspecifyVersion")}")
    implementation("com.ibm.icu:icu4j:${property("icu4jVersion")}")
    testRuntimeOnly("com.h2database:h2")
    // https://mvnrepository.com/artifact/am.ik.yavi/yavi
    implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")

    implementation("am.ik.yavi:yavi:${property("yaviVersion")}")
    annotationProcessor("cc.jilt:jilt:${property("jiltVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") {
        exclude("com.google.guava")
    }
    // https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber
    implementation("com.googlecode.libphonenumber:libphonenumber:${property("libphonenumberVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    testImplementation("org.assertj:assertj-db:${property("assertjDbVersion")}")

    // Apache POI for Excel processing
    implementation("org.apache.poi:poi:${property("poiVersion")}")
    implementation("org.apache.poi:poi-ooxml:${property("poiVersion")}")
}
tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).addStringOption("encoding", "UTF-8")
}

// flyway {
//    url = "jdbc:postgresql://localhost:5432/postgres"
//    user = "postgres"
//    password = "postgres"
//}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
        mavenBom("org.jmolecules:jmolecules-bom:${property("jmoleculesBomVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
    }
}
jacoco {
    toolVersion = "0.8.14"
    // reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}
tasks.test {
    useJUnitPlatform {
        excludeTags("medium", "large")
        timeout.set(Duration.ofSeconds(60))
    }
    finalizedBy(tasks.jacocoTestReport) // a report is always generated after tests run
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

apply(plugin = "com.github.ben-manes.versions")


tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
