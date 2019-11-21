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

    "Multiple recorded tasks are returned in order" {
        val recorder = TaskGraphRecorder()
        val task1 = mockk<TaskInfo>()
        val task2 = mockk<TaskInfo>()
        val task3 = mockk<TaskInfo>()
        recorder.recordTask(task1)
        recorder.recordTask(task2)
        recorder.recordTask(task3)
        recorder.executedTasks should containExactly(task1, task2, task3)
    }
})