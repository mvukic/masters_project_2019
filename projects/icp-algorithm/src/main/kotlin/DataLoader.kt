import models.Data
import models.Frame
import models.basic.EulerAngles
import models.basic.RotationMatrix
import models.basic.TransformMatrix
import models.basic.TranslationMatrix
import java.io.File

fun loadGroundTruth(rootPath: String): Data {
    val root = File(rootPath)
    val transformsDir = root.resolve("actor_transforms")
    val relativeTransformFile = root.resolve("relative_transform.txt")

    val files = transformsDir.walk().filter { it.isFile }.toList()
    val mapped = files.map { file ->
        // Use frame id as id
        val frameId = file.nameWithoutExtension
        // Parse ground truth transform
        val (timestamp, transform) = file.readLines().map { it.trim() }
        Frame(frameId, timestamp.toDouble(), transform.toTransform())
    }
    // Parse transform between lidar and vehicle
    val relativeTransform = relativeTransformFile.readText().trim().toTransform()
    return Data(
        groundTruth = mapped.sortedBy { it.frameId },
        lidarToVehicleTransform = relativeTransform
    )
}

fun loadICPResults(rootPath: String): List<TransformMatrix> {
    val root = File(rootPath)
    val icpResultsDir = root.resolve("icp_results")
    val files = icpResultsDir.walk().filter { it.isFile }.toList()
    return files.map { file ->
        val (first, second) = file.nameWithoutExtension.split("-")
        val lines = file.readLines().map { it.trim() }
        val innerMat = listOf(
            lines[1].split(Regex(" +")),
            lines[2].split(Regex(" +")),
            lines[3].split(Regex(" +"))
        )
        TransformMatrix(
            Pair(first, second),
            RotationMatrix.fromStringList(innerMat),
            TranslationMatrix.fromStringList(innerMat),
            lines[0].toDouble(),
            EulerAngles.fromString(lines[5].split(Regex(" +")))
        )
    }
}