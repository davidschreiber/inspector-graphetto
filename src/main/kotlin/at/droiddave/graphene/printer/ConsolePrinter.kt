package at.droiddave.graphene.printer

import io.bretty.console.tree.TreeNodeConverter
import io.bretty.console.tree.TreePrinter
import org.gradle.api.Task
import org.gradle.api.logging.Logger

internal class ConsolePrinter(private val logger: Logger) {
    fun print(tasks: Collection<Task>) {
        val converter = object : TreeNodeConverter<Task> {
            override fun children(t: Task): List<Task> {
                return t.taskDependencies.getDependencies(t).toList()
            }

            override fun name(t: Task): String {
                return t.path
            }
        }

        tasks.forEach { task ->
            logger.lifecycle(TreePrinter.toString(task, converter))
        }
    }
}