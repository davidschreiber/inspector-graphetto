package at.droiddave.graphene.tests

import at.droiddave.graphene.GraphenePlugin
import io.kotlintest.specs.StringSpec
import io.mockk.mockk
import org.gradle.api.Project

class GrapheneUnitTest : StringSpec({
    "Plugin can be instantiated" {
        GraphenePlugin()
    }

    "Plugin can be bound to project" {
        val plugin = GraphenePlugin()
        val project = mockk<Project>(relaxed = true)
        plugin.apply(project)
    }
})

