import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.60"
}

group = "at.droiddave.grapher"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions { jvmTarget = "1.8" }
compileTestKotlin.kotlinOptions { jvmTarget = "1.8" }

dependencies {
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")


    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
