package at.droiddave.graphetto.tests

import at.droiddave.graphetto.tests.utils.TestDirectoryListener
import at.droiddave.graphetto.tests.utils.gradleRunner
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.string.shouldNotContain
import io.kotlintest.specs.StringSpec

class ConsolePrintingIntegrationTest : StringSpec() {
    private val directoryRule = TestDirectoryListener()
    override fun listeners() = listOf(directoryRule)

    init {
        "Console tree printing" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/simple-project")
            projectDir.resolve("build.gradle").appendText("""
                graphetto {
                    consoleOutput = at.droiddave.graphetto.ConsoleOutput.TREE
                }
            """.trimIndent())

            val buildResult = gradleRunner(projectDir)
                .withArguments("someTask")
                .build()

            buildResult.output shouldContain """
                ── :someTask
                    └── :someOtherTask
            """.trimIndent()
        }

        "Defaults to no console tree printing" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/simple-project")

            val buildResult = gradleRunner(projectDir)
                .withArguments("someTask")
                .build()

            buildResult.output shouldNotContain """
                ── :someTask
                    └── :someOtherTask
            """.trimIndent()
        }

        "Use system option to enable tree printing" {
            val projectDir = directoryRule.initializeWithResourceDirectory("/fixtures/simple-project")

            val buildResult = gradleRunner(projectDir)
                .withArguments("someTask", "-Dat.droiddave.graphetto.consoleOutput=TREE")
                .build()

            buildResult.output shouldContain """
                ── :someTask
                    └── :someOtherTask
            """.trimIndent()
        }
    }

}
