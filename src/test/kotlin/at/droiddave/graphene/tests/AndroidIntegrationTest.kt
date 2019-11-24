package at.droiddave.graphene.tests

import at.droiddave.graphene.tests.utils.TestDirectoryListener
import at.droiddave.graphene.tests.utils.loadGraphFromFile
import at.droiddave.graphene.tests.utils.loadGraphFromTestResources
import at.droiddave.graphene.tests.utils.shouldBeIsomorphTo
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner

class AndroidIntegrationTest : StringSpec() {
    private val directoryRule = TestDirectoryListener()
    override fun listeners() = listOf(directoryRule)

    init {
        "Test plugin instantiation using plugin ID" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-3.5.2")
            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .build()
        }

        "Report task graph for :assembleDebug" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/android-agp-3.5.2")
            val buildResult = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
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

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments("assembleDebug")
                .build()

            val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val actualGraph = loadGraphFromFile(reportFile)
            val expectedGraph = loadGraphFromTestResources("/results/android-agp-3.5.2.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }
    }
}
