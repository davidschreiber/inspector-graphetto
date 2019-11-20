package at.droiddave.grapher.tests

import at.droiddave.grapher.TaskGraphRecorder
import at.droiddave.grapher.TaskInfo
import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.collections.containExactly
import io.kotlintest.should
import io.kotlintest.specs.StringSpec
import io.mockk.mockk

class TaskGraphRecorderTest : StringSpec({
    "New recorder returns empty list" {
        val recorder = TaskGraphRecorder()
        recorder.executedTasks should beEmpty()
    }

    "Recorded task should be returned" {
        val recorder = TaskGraphRecorder()
        val taskInfo = mockk<TaskInfo>()
        recorder.recordTask(taskInfo)
        recorder.executedTasks should containExactly(taskInfo)
    }
})