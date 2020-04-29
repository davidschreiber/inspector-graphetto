package at.droiddave.graphetto.tests

import at.droiddave.graphetto.tests.utils.*
import io.kotlintest.specs.StringSpec

class AndroidIntegrationTest : StringSpec() {
    private val directoryRule = TestDirectoryListener()
    override fun listeners() = listOf(directoryRule)

    init {
        "Test plugin instantiation using plugin ID" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-3.5.2")
            gradleRunner(projectDir)
                .build()
        }

        "Report task graph for :assembleDebug" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-3.5.2")
            gradleRunner(projectDir)
                .withArguments("assembleDebug")
                .build()

            val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val actualGraph = loadGraphFromFile(reportFile)
            val expectedGraph = loadGraphFromTestResources("/results/android-agp-3.5.2.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }

        "Works if report file already exists" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-3.5.2")
            projectDir.resolve("build/reports/taskGraph/graph.dot").apply {
                parentFile.mkdirs()
                writeText("some content")
            }

            gradleRunner(projectDir)
                .withArguments("assembleDebug")
                .build()

            val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val actualGraph = loadGraphFromFile(reportFile)
            val expectedGraph = loadGraphFromTestResources("/results/android-agp-3.5.2.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }
    }
}
