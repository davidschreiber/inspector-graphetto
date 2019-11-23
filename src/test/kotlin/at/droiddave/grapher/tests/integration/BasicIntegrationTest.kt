package at.droiddave.grapher.tests.integration

import at.droiddave.grapher.tests.utils.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner

class BasicIntegrationTest : StringSpec() {
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

        "Logs default task" {
            val projectDir = tempDir.get()
            val buildFile = projectDir.resolve("build.gradle")
            buildFile.writeText("""
                plugins {
                    id('at.droiddave.grapher')
                }
                
                tasks.register('someTask') {
                    dependsOn 'someOtherTask'
                }
                
                tasks.register('someOtherTask') 
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments("someTask")
                .build()

            val graphReportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val actualGraph = loadGraphFromFile(graphReportFile)
            val expectedGraph = loadGraphFromString("""
                    strict digraph G {
                      1 [ label=":someOtherTask" ];
                      2 [ label=":someTask" ];
                      2 -> 1;
                    }
                    """.trimIndent()
            )
            actualGraph shouldBeIsomorphTo expectedGraph
        }

        "Works if report file already exists" {

        }
    }

}
