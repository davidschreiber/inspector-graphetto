package at.droiddave.graphene.tests.integration

import at.droiddave.graphene.tests.utils.TestDirectoryListener
import at.droiddave.graphene.tests.utils.loadGraphFromFile
import at.droiddave.graphene.tests.utils.loadGraphFromTestResources
import at.droiddave.graphene.tests.utils.shouldBeIsomorphTo
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner

class AndroidIntegrationTest : StringSpec() {
    private val tempDir = TestDirectoryListener()
    override fun listeners() = listOf(tempDir)

    init {
        "Test plugin instantiation using plugin ID" {
            val buildFile = tempDir.get().resolve("build.gradle")
            buildFile.writeText("""
                buildscript {
                    repositories {
                        google()
                        jcenter()
                    }

                    dependencies {
                        classpath 'com.android.tools.build:gradle:3.5.2'
                    }
                }
                plugins {
                    id("at.droiddave.graphene")
                }
                apply plugin: 'com.android.application'

                android {
                    compileSdkVersion 29
                    buildToolsVersion "29.0.0"
                }
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tempDir.get())
                .build()
        }

        "Report task graph for :assembleDebug" {
            val projectDir = tempDir.get()
            projectDir.resolve("src/main/AndroidManifest.xml").apply {
                parentFile.mkdirs()
                writeText("""
                    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                        package="at.droiddave.graphene.testapp"
                        xmlns:tools="http://schemas.android.com/tools"/>
                """.trimIndent())
            }
            projectDir.resolve("build.gradle").writeText("""
                buildscript {
                    repositories {
                        google()
                        jcenter()
                    }
                    
                    dependencies {
                        classpath 'com.android.tools.build:gradle:3.5.2'
                    }
                }
                plugins {
                    id('at.droiddave.graphene')
                }
                repositories {
                    google()
                    jcenter()
                }
                apply plugin: 'com.android.application'
                
                android {
                    compileSdkVersion 29
                    buildToolsVersion '29.0.0'
                    defaultConfig {
                        applicationId 'at.droiddave.graphene.testapp'
                    }
                }
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments("assembleDebug")
                .build()

            val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val actualGraph = loadGraphFromFile(reportFile)
            val expectedGraph = loadGraphFromTestResources("/android-agp-3.5.2.dot")
            actualGraph shouldBeIsomorphTo expectedGraph
        }

        "Works if report file already exists" {

        }
    }

}
