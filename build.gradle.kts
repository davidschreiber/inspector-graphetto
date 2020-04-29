import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Using Kotlin 1.3.50 since version 1.3.60 is not full compatible with coroutines and generates build errors in our tests.
    // Waiting for some version after 1.3.60 to see if it resolves the issue. Here's a related error with what happened during
    // the builds (not exact though): https://youtrack.jetbrains.com/issue/KT-34527
    kotlin("jvm") version "1.3.50"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.11.0"
}

group = "at.droiddave.graphetto"
version = "0.0.1"

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions { jvmTarget = "1.8" }
compileTestKotlin.kotlinOptions { jvmTarget = "1.8" }

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jgrapht:jgrapht-core:1.3.0")
    implementation("org.jgrapht:jgrapht-io:1.3.0")
    implementation("io.bretty:console-tree-builder:2.0")
    
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.0")
    testImplementation(gradleTestKit())
}

tasks.withType<Test> {
    @Suppress("UnstableApiUsage")
    useJUnitPlatform()
    // Since Graphetto outputs UTF-8 characters on the conosle, make sure tests run with UTF-8 encoding.
    jvmArgs("-Dfile.encoding=utf-8")
}

gradlePlugin {
    plugins {
        create("graphetto") {
            id = "at.droiddave.graphetto"
            implementationClass = "at.droiddave.graphetto.GraphettoPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/davidschreiber/graphetto"
    vcsUrl = "https://github.com/davidschreiber/graphetto"
    (plugins) {
        "graphetto" {
            displayName = "Inspector Graphetto"
            description = "Inspector Graphetto is a Gradle build plugin to inspect and visualize the task execution " +
                    "graph of your build."
            tags = listOf("graphetto", "gradle-task", "task-graph", "tasks", "graph", "graphviz", "dot")
        }
    }
}