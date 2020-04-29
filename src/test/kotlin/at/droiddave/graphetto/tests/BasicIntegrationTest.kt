package at.droiddave.graphetto.tests

import at.droiddave.graphetto.tests.utils.*
import io.kotlintest.specs.StringSpec

class BasicIntegrationTest : StringSpec() {
    private val directoryRule = TestDirectoryListener()
    override fun listeners() = listOf(directoryRule)

    init {
        "Test plugin instantiation using plugin ID" {
            val projectDir = directoryRule.get()
            val buildFile = projectDir.resolve("build.gradle")
            buildFile.writeText("""
                plugins {
                    id("at.droiddave.graphetto")
                }
            """.trimIndent())

            gradleRunner(projectDir)
                .build()
        }

        "Logs default task" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/simple-project")

            gradleRunner(projectDir)
                .withArguments("someTask")
                .build()

            val graphReportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val actualGraph = loadGraphFromFile(graphReportFile)
            val expectedGraph = loadGraphFromTestResources("/results/simple-project.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }

        "Non default report file path" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/simple-project")
            projectDir.resolve("build.gradle").appendText("""
                graphetto {
                    outputFile = new File("${'$'}buildDir/report.dot")
                }
            """.trimIndent())

            gradleRunner(projectDir)
                .withArguments("someTask")
                .build()

            val graphReportFile = projectDir.resolve("build/report.dot")
            val actualGraph = loadGraphFromFile(graphReportFile)
            val expectedGraph = loadGraphFromTestResources("/results/simple-project.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }
    }

}
