package at.droiddave.graphene

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.jgrapht.io.DOTExporter
import org.jgrapht.io.IntegerComponentNameProvider
import org.jgrapht.io.StringComponentNameProvider

class GraphenePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val outputFile = target.buildDir.resolve("reports/taskGraph/graph.dot")
        outputFile.apply {
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                error("Error while creating output directory: ${parentFile.absolutePath}")
            }
        }
        target.gradle.taskGraph.addTaskExecutionGraphListener {
            val dag = DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java)
            it.allTasks.forEach { task ->
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
}