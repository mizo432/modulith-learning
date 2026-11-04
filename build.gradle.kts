
// SonarQube プラグインを全サブプロジェクトに適用
subprojects {
    apply(plugin = "org.sonarqube")
}

sonarqube {
    properties {
        property("sonar.projectKey", "modulith-learning")
        property("sonar.organization", "modulith-learning")
        // SonarCloud の場合
        property("sonar.host.url", "https://sonarcloud.io")
        // 自前 SonarQube の場合は以下を有効化
        // property("sonar.host.url", "http://localhost:9000")
    }
}
