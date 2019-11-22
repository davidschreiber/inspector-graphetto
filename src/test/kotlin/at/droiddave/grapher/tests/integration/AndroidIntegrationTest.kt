package at.droiddave.grapher.tests.integration

import at.droiddave.grapher.tests.utils.TestDirectoryListener
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.jgrapht.io.DOTImporter
import org.jgrapht.io.EdgeProvider
import org.jgrapht.io.VertexProvider

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
                    id("at.droiddave.grapher")
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
                        package="at.droiddave.grapher.testapp"
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
                    id('at.droiddave.grapher')
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
                        applicationId 'at.droiddave.grapher.testapp'
                    }
                }
            """.trimIndent())

            GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments("assembleDebug")
                .build()

            val reportFile = projectDir.resolve("build/reports/taskGraph/graph.dot")
            val importer = DOTImporter<String, DefaultEdge>(
                VertexProvider<String> { _, attributes -> attributes["label"].toString() },
                EdgeProvider<String, DefaultEdge> { _, _, _, _ -> DefaultEdge() }
            )
            val reportedGraph = DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java)
            importer.importGraph(reportedGraph, reportFile)

            val expectedGraph = DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java)
            importer.importGraph(expectedGraph, javaClass.getResource("/android-agp-3.5.2.dot").openStream())

            VF2GraphIsomorphismInspector(reportedGraph, expectedGraph).isomorphismExists() shouldBe true
        }

        "Works if report file already exists" {

        }
    }

}
