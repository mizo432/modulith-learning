import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.time.Duration
import net.ltgt.gradle.errorprone.errorprone

buildscript {
    repositories {
        mavenCentral() // Maven Centralリポジトリから依存関係を取得する
    }
    dependencies {
        // Flyway（データベース移行ツール）のクラスパス追加
        classpath("org.flywaydb:flyway-database-postgresql:12.11.0")
    }
}
plugins {
    // Javaプラグインを適用（Javaプロジェクトのサポート）
    java
    // Spring Bootプラグイン
    id("org.springframework.boot") version "4.2.0"
    // Spring関連の依存関係の管理用プラグイン
    id("io.spring.dependency-management") version "1.1.7"
    id("se.patrikerdes.use-latest-versions") version "0.2.19"
    id("com.github.ben-manes.versions") version "0.53.0"
    // Flywayプラグイン（DBマイグレーション）
    id("org.flywaydb.flyway") version "12.11.0"
    jacoco
    // Pitest ミューテーションテストプラグイン
    id("info.solidsoft.pitest") version "1.19.0"
    // Error Prone プラグイン（NullAway 用）
    id("net.ltgt.errorprone") version "4.1.0"
    id("net.ltgt.nullaway") version "3.3.0"
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
        // Error Prone / NullAway 設定
        options.errorprone {
            // 他の Error Prone チェックを全て無効化し、NullAway のみを有効にする
            disableAllChecks.set(true)
            error("NullAway")
            errorproneArgs.addAll(
                listOf(
                    "-XepExcludedPaths:.*/src/test/.*",
                )
            )
        }
    }
    // properties ファイルを UTF-8 で処理する
    processResources {
        filteringCharset = "UTF-8"
    }
}

nullaway {
    onlyNullMarked = true
    jspecifyMode = true
}
configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "2.2.0"
extra["springModulithInsightVersion"] = "2.2.0"
extra["guavaVersion"] = "33.6.0-jre"
extra["icu4jVersion"] = "77.1"
extra["yaviVersion"] = "0.16.0"
extra["jiltVersion"] = "1.8.2"
extra["jdbcPostgresqlVersion"] = "11.3.4"
extra["openapiUiVersion"] = "2.8.9"
extra["jmoleculesBomVersion"] = "2023.2.1"
extra["archunitVersion"] = "1.5.1"
extra["junitVersion"] = "6.1.0"
// Override Spring Boot BOM to align all JUnit artifacts (Jupiter, Platform, Vintage)
extra["junit-jupiter.version"] = "6.1.0"
extra["springCloudBomVersion"] = "2026.0.0"
extra["spotbugsAnnotationVersion"] = "4.9.3"
extra["libphonenumberVersion"] = "9.0.9"
extra["poiVersion"] = "5.5.0"
extra["jspecifyVersion"] = "1.0.1"
extra["assertjDbVersion"] = "3.0.2"
extra["superCsvVersion"] = "2.4.0"
extra["testcontainersVersion"] = "2.0.5"
extra["nullawayVersion"] = "0.12.13"
extra["errorproneVersion"] = "2.43.0"
extra["awsSdkVersion"] = "2.59.7"

// Force JUnit Platform artifacts to align with JUnit 6 version
// Spring Boot 4.1.1 manages junit-platform-launcher at 1.14.4, but JUnit 6 requires 6.x
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.junit.platform") {
            useVersion("6.1.0")
        }
    }
}

dependencies {
    implementation(project(":mizo432-shared-common"))
    // Error Prone / NullAway
    errorprone("com.google.errorprone:error_prone_core:${property("errorproneVersion")}")
    errorprone("com.uber.nullaway:nullaway:${property("nullawayVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-starter-jpa")
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
    runtimeOnly("org.springframework.modulith:spring-modulith-observability")
    runtimeOnly("com.julien-dubois.bootui:bootui-spring-boot-starter:2.5.0")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    implementation("org.jmolecules:jmolecules-onion-architecture")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("openapiUiVersion")}")
    testImplementation("com.github.spotbugs:spotbugs-annotations:${property("spotbugsAnnotationVersion")}")
    runtimeOnly("org.springframework.modulith:spring-modulith-starter-insight:${property("springModulithInsightVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${property("junitVersion")}")
    implementation("com.google.guava:guava:${property("guavaVersion")}")
    implementation("org.jspecify:jspecify:${property("jspecifyVersion")}")
    implementation("com.ibm.icu:icu4j:${property("icu4jVersion")}")
    testRuntimeOnly("com.h2database:h2")
    // https://mvnrepository.com/artifact/am.ik.yavi/yavi
    implementation("am.ik.yavi:yavi:${property("yaviVersion")}")
    annotationProcessor("cc.jilt:jilt:${property("jiltVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") {
        exclude("com.google.guava")
    }
    // https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber
    implementation("com.googlecode.libphonenumber:libphonenumber:${property("libphonenumberVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.jasypt:jasypt:1.9.3")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    testImplementation("org.assertj:assertj-db:${property("assertjDbVersion")}")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:testcontainers-postgresql:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:${property("testcontainersVersion")}")
    implementation("com.tngtech.archunit:archunit:${property("archunitVersion")}")
    testImplementation("com.tngtech.archunit:archunit-junit5:${property("archunitVersion")}")

    // Apache POI for Excel processing
    implementation("org.apache.poi:poi:${property("poiVersion")}")
    implementation("org.apache.poi:poi-ooxml:${property("poiVersion")}")

    // Spring Batch & Super CSV
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.batch:spring-batch-core")
    implementation("org.springframework.batch:spring-batch-infrastructure")
    implementation("net.sf.supercsv:super-csv:${property("superCsvVersion")}")
    testImplementation("org.springframework.batch:spring-batch-test")

    // Flyway PostgreSQLサポート
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.flywaydb:flyway-core")
    // 帳票管理: MinIO（S3互換）へのアクセスに AWS SDK v2 の S3 クライアントを使う
    implementation("software.amazon.awssdk:s3:${property("awsSdkVersion")}")
}
tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).addStringOption("encoding", "UTF-8")
}

// 事業領域ごとのFlyway構成
// data-designスキルに従い、事業領域ごとにスキーマを分離する
flyway {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
}

// 事業領域: 関連付け (AS)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateAs") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_as")
    locations = arrayOf("classpath:db/migration/as")
}

// 事業領域: 開発運用 (DO)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateDo") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_do")
    locations = arrayOf("classpath:db/migration/do")
}

// 事業領域: 電子帳票 (EF)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateEf") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_ef")
    locations = arrayOf("classpath:db/migration/ef")
}

// 事業領域: 購買事業 (PC)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigratePc") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_pc")
    locations = arrayOf("classpath:db/migration/pc")
}

// 事業領域: 承認申請 (AR)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateAr") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_ar")
    locations = arrayOf("classpath:db/migration/ar")
}

// 事業領域: 認可 (AZ)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateAz") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_az")
    locations = arrayOf("classpath:db/migration/az")
}

// 事業領域: 通知 (NT)
tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateNt") {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("undecided_nt")
    locations = arrayOf("classpath:db/migration/nt")
}

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
    // ArchUnitがmainクラスをスキャンできるようにclasspathに追加
    classpath += sourceSets["main"].output
    finalizedBy(tasks.jacocoTestReport) // a report is always generated after tests run
}

val mediumTest = tasks.register("mediumTest", Test::class.java) {
    description = "medium sized test"
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform {
        includeTags("medium")
    }
    timeout.set(Duration.ofMinutes(60))
    // Issue #737: クラスごとにJVMを分離し、共有DB・コンテナ・スキーマを介したクラス間干渉を防ぐ
    forkEvery = 1
    // Issue #751: フォークは別JVM・別コンテナのため干渉対策を維持したまま並列実行できる
    maxParallelForks = (project.findProperty("mediumMaxParallelForks") as String?)?.toIntOrNull() ?: 1
    // Issue #751: small は CI の Backend Small Tests ジョブで並列実行するため、test への依存は持たない
}
val largeTest = tasks.register("largeTest", Test::class.java) {
    description = "large sized test"
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
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

// Pitest ミューテーションテスト構成
// 初回導入時はcom.github.mizo432.sharedパッケージに限定し、実行時間を抑える
pitest {
    targetClasses = setOf("com.github.mizo432.shared.*")
    // JUnit 5 プラグインの自動追加
    junit5PluginVersion = "1.2.1"
    threads = Runtime.getRuntime().availableProcessors()
    outputFormats = setOf("HTML", "XML")
    timestampedReports = false
    // 不安定なテストをスキップして、ミューテーションテストを継続する
    skipFailingTests = true
    excludedClasses = setOf(
        // 生成コード・インフラストラクチャコードは除外
        "com.github.mizo432.shared.*.package-info",
        "com.github.mizo432.shared.*.dto.*",
        "com.github.mizo432.shared.*.config.*"
    )
    jvmArgs = listOf("-Xmx1024m")
}
