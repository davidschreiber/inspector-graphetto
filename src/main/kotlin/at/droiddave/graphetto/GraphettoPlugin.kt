package at.droiddave.graphetto

import at.droiddave.graphetto.printer.ConsolePrinter
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

open class GraphettoExtension(project: Project) {
    val outputFile = project.objects.fileProperty().apply {
        set(project.buildDir.resolve("reports/taskGraph/graph.dot"))
    }

    val consoleOutput = project.objects.property(ConsoleOutput::class.java).apply {
        set(ConsoleOutput.NONE)
    }
}

class GraphettoPlugin : Plugin<Project> {
    lateinit var extension: GraphettoExtension

    override fun apply(target: Project) {
        extension = target.extensions.create("graphetto", GraphettoExtension::class.java, target)
        target.gradle.taskGraph.addTaskExecutionGraphListener { taskGraph ->
            val consoleOutput = target
                .gradle
                .startParameter
                .systemPropertiesArgs["at.droiddave.graphetto.consoleOutput"]
                ?.let { ConsoleOutput.valueOf(it) }
                ?: extension.consoleOutput.get()
            if (consoleOutput == ConsoleOutput.TREE) {
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
//        GraphVizRenderer().render(outputFile, outputFile.parentFile.resolve("graph.png"), OutputFormat.PNG)
    }
}