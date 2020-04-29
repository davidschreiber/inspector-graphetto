package at.droiddave.graphetto.tests

import at.droiddave.graphetto.tests.utils.*
import io.kotlintest.specs.FunSpec
import io.kotlintest.specs.StringSpec

class AndroidIntegrationTest : FunSpec() {
    private val directoryRule = TestDirectoryListener()
    override fun listeners() = listOf(directoryRule)

    init {
        val androidGradlePluginVersions = listOf("3.5.2", "3.6.3")
        androidGradlePluginVersions.forEach { androidGradlePluginVersion ->
            context("With Android Gradle Plugin $androidGradlePluginVersion") {
                test("Test plugin instantiation using plugin ID") {
                    val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-$androidGradlePluginVersion")
                    gradleRunner(projectDir)
                            .build()
                }

                test("Report task graph for :assembleDebug") {
                    val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-$androidGradlePluginVersion")
                    gradleRunner(projectDir)
                            .withArguments("assembleDebug")
                            .build()

                    val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
                    val actualGraph = loadGraphFromFile(reportFile)
                    val expectedGraph = loadGraphFromTestResources("/results/android-agp-$androidGradlePluginVersion.dot")
                    actualGraph shouldBeIsomorphTo expectedGraph
                }

                test("Works if report file already exists") {
                    val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-$androidGradlePluginVersion")
                    projectDir.resolve("build/reports/taskGraph/graph.dot").apply {
                        parentFile.mkdirs()
                        writeText("some content")
                    }

                    gradleRunner(projectDir)
                            .withArguments("assembleDebug")
                            .build()

                    val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
                    val actualGraph = loadGraphFromFile(reportFile)
                    val expectedGraph = loadGraphFromTestResources("/results/android-agp-$androidGradlePluginVersion.dot")
                    actualGraph shouldBeIsomorphTo expectedGraph
                }
            }
        }
    }
}
