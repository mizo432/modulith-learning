plugins {
    id("com.github.node-gradle.node") version "7.0.2"
}

node {
    version.set("20.12.2")
    npmVersion.set("10.5.0")
    download.set(true)
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmBuild") {
    description = "Build the frontend application for production"
    args.set(listOf("run", "build:production"))
    dependsOn("npmInstall")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmBuildStaging") {
    description = "Build the frontend application for staging"
    args.set(listOf("run", "build:staging"))
    dependsOn("npmInstall")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmStart") {
    description = "Start the development server with default environment"
    args.set(listOf("run", "start"))
    dependsOn("npmInstall")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmStartLocal") {
    description = "Start the development server with local environment"
    args.set(listOf("run", "start:local"))
    dependsOn("npmInstall")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmStartDev") {
    description = "Start the development server with development environment"
    args.set(listOf("run", "start:dev"))
    dependsOn("npmInstall")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmTest") {
    description = "Run frontend tests"
    args.set(listOf("run", "test", "--", "--watchAll=false"))
    dependsOn("npmInstall")
}

tasks.register<Delete>("clean") {
    delete("build", "node_modules")
}
