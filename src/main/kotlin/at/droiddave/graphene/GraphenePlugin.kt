package at.droiddave.graphene

import at.droiddave.graphene.printer.ConsolePrinter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.jgrapht.io.DOTExporter
import org.jgrapht.io.IntegerComponentNameProvider
import org.jgrapht.io.StringComponentNameProvider

enum class ConsoleOutput {
    TREE,
    NONE
}

open class GrapheneExtension(project: Project) {
    val outputFile = project.objects.fileProperty().apply {
        set(project.buildDir.resolve("reports/taskGraph/graph.dot"))
    }

    val consoleOutput = project.objects.property(ConsoleOutput::class.java).apply {
        set(ConsoleOutput.NONE)
    }
}

class GraphenePlugin : Plugin<Project> {
    lateinit var extension: GrapheneExtension

    override fun apply(target: Project) {
        extension = target.extensions.create("graphene", GrapheneExtension::class.java, target)
        target.gradle.taskGraph.addTaskExecutionGraphListener { taskGraph ->
            if (extension.consoleOutput.get() == ConsoleOutput.TREE) {
                printTaskTreeToConsole(target)
            }

            createTaskGraphReport(taskGraph)
        }
    }

    private fun printTaskTreeToConsole(project: Project) {
        val printer = ConsolePrinter(project.logger)
        printer.print(project.gradle.startParameter.taskNames.map { project.tasks.getByName(it) })
    }

    private fun createTaskGraphReport(taskGraph: TaskExecutionGraph) {
        val outputFile = extension.outputFile.get().asFile.apply {
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                error("Error while creating output directory: ${parentFile.absolutePath}")
            }
        }

        val dag = DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java)
        taskGraph.allTasks.forEach { task ->
            dag.addVertex(task.path)
            task.taskDependencies.getDependencies(task).forEach { dependency ->
                dag.addEdge(task.path, dependency.path)
            }
        }

        val exporter = DOTExporter<String, DefaultEdge>(
            IntegerComponentNameProvider<String>(),
            StringComponentNameProvider<String>(),
            null
        )
        exporter.exportGraph(dag, outputFile)
    }
}