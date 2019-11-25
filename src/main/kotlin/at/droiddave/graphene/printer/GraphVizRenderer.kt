package at.droiddave.graphene.printer

import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import java.io.File

enum class OutputFormat {
    PNG, DOT,
}
internal class GraphVizRenderer {
    fun render(dotFile: File, outputFile: File, outputFormat: OutputFormat) {
        val renderFormat = when(outputFormat) {
            OutputFormat.PNG -> Format.PNG
            else -> error("Unsupported output format.")
        }

        Graphviz.fromFile(dotFile)
            .render(renderFormat)
            .toFile(outputFile)
    }
}