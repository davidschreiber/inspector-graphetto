package at.droiddave.graphetto.tests.utils

import io.kotlintest.shouldBe
import org.jgrapht.Graph
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.jgrapht.io.DOTImporter
import org.jgrapht.io.EdgeProvider
import org.jgrapht.io.VertexProvider
import java.io.File
import java.io.IOException
import java.io.InputStream

/** Shared importer instance for loading a DAG from a .dot file. */
private val dotFileImporter: DOTImporter<String, DefaultEdge> by lazy {
    DOTImporter<String, DefaultEdge>(
        VertexProvider<String> { _, attributes -> attributes["label"].toString() },
        EdgeProvider<String, DefaultEdge> { _, _, _, _ -> DefaultEdge() }
    )
}

fun loadGraphFromFile(file: File) = loadGraphFromStream(file.inputStream())

/**
 * Imports a directed acyclic graph from the given .dot file string.
 */
fun loadGraphFromString(dotFileContent: String) = loadGraphFromStream(dotFileContent.byteInputStream())

fun loadGraphFromTestResources(resourcePath: String) = loadGraphFromStream(
    dotFileImporter.javaClass.getResourceAsStream(resourcePath)
        ?: throw IOException("No such test resource: $resourcePath")
)

/**
 * Imports a directed acyclic graph from the given input stream.
 */
fun loadGraphFromStream(inputStream: InputStream): DirectedAcyclicGraph<String, DefaultEdge> =
    DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java).apply {
        inputStream.use {
            dotFileImporter.importGraph(this, inputStream)
        }
    }

/**
 * Matcher for testing whether two graphs are isomorph (i.e. equal) or not.
 */
infix fun <V, E> Graph<V, E>.shouldBeIsomorphTo(other: Graph<V, E>?) {
    VF2GraphIsomorphismInspector(this, other).isomorphismExists() shouldBe true
}
