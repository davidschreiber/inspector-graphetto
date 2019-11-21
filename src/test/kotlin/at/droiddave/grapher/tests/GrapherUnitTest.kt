package at.droiddave.grapher.tests

import at.droiddave.grapher.GrapherPlugin
import io.kotlintest.specs.StringSpec
import io.mockk.mockk
import org.gradle.api.Project

class GrapherUnitTest : StringSpec({
    "Plugin can be instantiated" {
        GrapherPlugin()
    }

    "Plugin can be bound to project" {
        val plugin = GrapherPlugin()
        val project = mockk<Project>(relaxed = true)
        plugin.apply(project)
    }
})

