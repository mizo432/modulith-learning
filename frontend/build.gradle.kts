plugins {
    id("com.github.node-gradle.node") version "7.0.2"
}

node {
    version.set("20.12.2")
    npmVersion.set("10.5.0")
    download.set(true)
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("installNpm") {
    description = "Install npm dependencies"
    args.set(listOf("install"))
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmBuild") {
    description = "Build the frontend application"
    args.set(listOf("run", "build"))
    dependsOn("npmInstall")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmStart") {
    description = "Start the development server"
    args.set(listOf("run", "start"))
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
