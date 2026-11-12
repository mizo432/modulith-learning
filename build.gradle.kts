plugins {
    id("org.sonarqube") version "6.2.0.5505"
}

sonar {
  properties {
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.projectKey", "modulith-learning")
    property("sonar.organization", "modulith-learning")
    property("sonar.projectName", "modulith-learning")
    property("sonar.sourceEncoding", "UTF-8")
    property("sonar.sources", ".")
    property("sonar.exclusions", "**/build/**,**/test/**,**/mediumTest/**,**/largeTest/**,**/node_modules/**,**/generated/**,**/*Test.java,**/*Tests.java,**/test/**,**/resources/**,**/*.properties,**/*.xml,**/db/migration/**")
    property("sonar.pullrequest.github.enabled", "true")
    property("sonar.pullrequest.github.repository", "modulith-learning")
    property("sonar.qualitygate.wait", "true")
  }
}

subprojects {
    apply(plugin = "org.sonarqube")
}
