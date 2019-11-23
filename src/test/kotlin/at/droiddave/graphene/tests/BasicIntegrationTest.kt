package at.droiddave.graphene.tests

import at.droiddave.graphene.tests.utils.TestDirectoryListener
import at.droiddave.graphene.tests.utils.loadGraphFromFile
import at.droiddave.graphene.tests.utils.loadGraphFromTestResources
import at.droiddave.graphene.tests.utils.shouldBeIsomorphTo
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner

class BasicIntegrationTest : StringSpec() {
    private val directoryRule = TestDirectoryListener()
    override fun listeners() = listOf(directoryRule)

    init {
        "Test plugin instantiation using plugin ID" {
            val buildFile = directoryRule.get().resolve("build.gradle")
            buildFile.writeText("""
                plugins {
                    id("at.droiddave.graphene")
                }
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(directoryRule.get())
                .build()
        }

        "Logs default task" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/simple-project")

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
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
                graphene {
                    outputFile = new File("${'$'}buildDir/report.dot")
                }
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments("someTask")
                .build()

            val graphReportFile = projectDir.resolve("build/report.dot")
            val actualGraph = loadGraphFromFile(graphReportFile)
            val expectedGraph = loadGraphFromTestResources("/results/simple-project.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }
    }

}
