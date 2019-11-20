package at.droiddave.grapher.tests

import at.droiddave.grapher.GrapherPlugin
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph

class GrapherUnitTest : StringSpec({
    "Plugin can be instantiated" {
        GrapherPlugin()
    }

    "Plugin can be bound to project" {
        val plugin = GrapherPlugin()
        val project = mockk<Project>(relaxed = true)
        plugin.apply(project)
    }

    "Plugin registers task graph listener" {
        val taskExecutionGraph = mockk<TaskExecutionGraph>(relaxed = true)
        val project = mockk<Project> {
            every { gradle } returns mockk {
                every { taskGraph } returns taskExecutionGraph
            }
        }
        val plugin = GrapherPlugin()
        plugin.apply(project)
        verify { taskExecutionGraph.addTaskExecutionListener(isNull(inverse = true)) }
    }
})

