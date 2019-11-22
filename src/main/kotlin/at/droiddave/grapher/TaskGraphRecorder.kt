package at.droiddave.grapher

class TaskGraphRecorder {
    val executedTasks: List<TaskInfo> get() = recordedTasks
    private val recordedTasks = mutableListOf<TaskInfo>()

    fun recordTask(taskInfo: TaskInfo) {
        recordedTasks.add(taskInfo)
    }
}