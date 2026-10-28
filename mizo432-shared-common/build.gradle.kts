plugins {
    java
    id("info.solidsoft.pitest") version "1.19.0"
}

version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

extra["guavaVersion"] = "33.6.0-jre"
extra["jspecifyVersion"] = "1.0.0"
extra["icu4jVersion"] = "77.1"
extra["uuidGeneratorVersion"] = "5.1.0"
extra["lombokVersion"] = "1.18.38"
extra["commonsLang3Version"] = "3.17.0"
extra["jakartaValidationApiVersion"] = "3.1.1"
extra["slf4jVersion"] = "2.0.17"
extra["junitVersion"] = "6.1.0"
extra["assertjVersion"] = "3.27.3"
extra["mockitoVersion"] = "5.15.2"

dependencies {
    compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
    implementation("com.google.guava:guava:${property("guavaVersion")}")
    implementation("org.jspecify:jspecify:${property("jspecifyVersion")}")
    implementation("jakarta.validation:jakarta.validation-api:${property("jakartaValidationApiVersion")}")
    implementation("org.apache.commons:commons-lang3:${property("commonsLang3Version")}")
    implementation("com.ibm.icu:icu4j:${property("icu4jVersion")}")
    implementation("com.fasterxml.uuid:java-uuid-generator:${property("uuidGeneratorVersion")}")
    implementation("org.slf4j:slf4j-api:${property("slf4jVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junitVersion")}")
    testImplementation("org.assertj:assertj-core:${property("assertjVersion")}")
    testImplementation("org.mockito:mockito-core:${property("mockitoVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junitVersion")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-Dnet.bytebuddy.experimental=true")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:unchecked")
    }
}

// Pitest ミューテーションテスト構成
pitest {
    targetClasses = setOf("com.github.mizo432.shared.common.*")
    // JUnit 5 プラグインの自動追加
    junit5PluginVersion = "1.2.1"
    threads = Runtime.getRuntime().availableProcessors()
    outputFormats = setOf("HTML", "XML")
    timestampedReports = false
    // 不安定なテストをスキップして、ミューテーションテストを継続する
    skipFailingTests = true
}
