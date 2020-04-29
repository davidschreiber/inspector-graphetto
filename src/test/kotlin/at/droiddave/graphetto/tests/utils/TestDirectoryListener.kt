package at.droiddave.graphetto.tests.utils

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TestListener
import java.io.File

class TestDirectoryListener : TestListener {
    private val testDirectory: File = File.createTempFile("temp-folder", "")

    fun get(): File = testDirectory

    override fun beforeTest(testCase: TestCase) {
        testDirectory.deleteRecursively()
        testDirectory.mkdir()
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        testDirectory.deleteRecursively()
    }

    fun initializeWithResourceDirectory(resourceDirectoryPath: String): File {
        val file = File(javaClass.getResource(resourceDirectoryPath).toURI())
        if (!file.isDirectory) error("Not a directory: ${file.absolutePath}")
        file.copyRecursively(testDirectory, overwrite = true)
        return testDirectory
    }
}