package at.droiddave.grapher.tests.utils

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TestListener
import java.io.File

class TestDirectoryListener : TestListener {
    private val testDirectory: File = File.createTempFile("temp-folder", "")

    fun get(): File = testDirectory

    override fun beforeTest(testCase: TestCase) {
        testDirectory.delete()
        testDirectory.mkdir()
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        testDirectory.delete()
    }
}