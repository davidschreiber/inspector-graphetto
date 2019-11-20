package at.droiddave.grapher.tests

import at.droiddave.grapher.tests.utils.TestDirectoryListener
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner

class GrapherPluginIntegrationTest : StringSpec() {
    private val tempDir = TestDirectoryListener()
    override fun listeners() = listOf(tempDir)

    init {
        "Test plugin instantiation using plugin ID" {
            val buildFile = tempDir.get().resolve("build.gradle")
            buildFile.writeText("""
                plugins {
                    id("at.droiddave.grapher")
                }
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tempDir.get())
                .build()
        }
    }

}
