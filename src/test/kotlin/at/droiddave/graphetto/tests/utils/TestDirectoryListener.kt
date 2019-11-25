package at.droiddave.graphetto.tests.utils

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TestListener
import java.io.File

class TestDirectoryListener : TestListener {
    private var testDirectory: File? = null
    private fun getTestDirectory(): File {
        val testDirectory = this.testDirectory ?: File.createTempFile("temp-folder", "")
        if (this.testDirectory == null) {
            this.testDirectory = testDirectory
        }
        return testDirectory
    }

    fun get(): File = getTestDirectory()

    override fun beforeTest(testCase: TestCase) {
        getTestDirectory().apply {
            deleteRecursively()
            mkdir()
        }
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        getTestDirectory().apply {
            deleteRecursively()
        }
        testDirectory = null
    }

    fun initializeWithResourceDirectory(resourceDirectoryPath: String): File {
        val file = File(javaClass.getResource(resourceDirectoryPath).toURI())
        if (!file.isDirectory) error("Not a directory: ${file.absolutePath}")
        val testDirectory = getTestDirectory()
        file.copyRecursively(testDirectory, overwrite = true)
        return testDirectory
    }
}