package at.droiddave.graphetto.tests.utils

import org.gradle.testkit.runner.GradleRunner
import java.io.File

fun gradleRunner(projectDir: File) = GradleRunner.create()
    .withProjectDir(projectDir)
    .forwardOutput()
    .withPluginClasspath()