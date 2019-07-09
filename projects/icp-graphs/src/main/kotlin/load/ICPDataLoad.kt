package load

import models.icp.RotationMatrix
import models.icp.TransformMatrix
import models.icp.TranslationMatrix
import models.shared.Euler
import java.io.File

fun loadICPResults(rootPath: String, take: Int = 999): List<TransformMatrix> {
    val root = File(rootPath)
    val icpResultsDir = root.resolve("icp_results")
    val files = icpResultsDir.walk().filter { it.isFile }.toList().take(take)
    return files.map { file ->
        val (first, second) = file.nameWithoutExtension.split("-")
        val lines = file.readLines().map { it.trim() }
        val upperMat = listOf(
            lines[1].split(Regex(" +")),
            lines[2].split(Regex(" +")),
            lines[3].split(Regex(" +"))
        )
        val rotMat = upperMat.map { it.take(3) }
        TransformMatrix(
            between = Pair(first, second),
            translation = TranslationMatrix.fromStringList(upperMat),
            fitness = lines[0].toDouble(),
            rotation = RotationMatrix.fromString(rotMat),
            angles = Euler.fromString(lines[5].split(Regex(" +"))),
            matrix = loadToMatrix(lines)
        )
    }.sortedBy { it.between.first }
}

fun loadToMatrix(lines: List<String>): Array<DoubleArray> {
    return arrayOf(
        lines[1].split(Regex(" +")).map { it.toDouble() }.toDoubleArray(),
        lines[2].split(Regex(" +")).map { it.toDouble() }.toDoubleArray(),
        lines[3].split(Regex(" +")).map { it.toDouble() }.toDoubleArray(),
        lines[4].split(Regex(" +")).map { it.toDouble() }.toDoubleArray()
    )

}