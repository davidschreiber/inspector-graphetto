package at.droiddave.grapher

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class GrapherPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.gradle.taskGraph.addTaskExecutionListener(object : TaskExecutionListener {
            override fun beforeExecute(task: Task) {
            }

            override fun afterExecute(task: Task, state: TaskState) {
            }
        })
    }
}