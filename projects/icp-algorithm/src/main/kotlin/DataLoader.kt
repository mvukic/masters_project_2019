import models.Data
import models.Frame
import models.basic.RotationMatrix
import models.basic.TransformMatrix
import models.basic.TranslationMatrix
import java.io.File

fun loadGroundTruth(rootPath: String, limit: Int = 0): Data {
    val root = File(rootPath)
    val transformsDir = root.resolve("actor_transforms")
    val relativeTransformFile = root.resolve("relative_transform.txt")

    val transformFiles = transformsDir.walk().filter { it.isFile }
    val files = if (limit > 0) transformFiles.take(limit).toList() else transformFiles.toList()
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

fun loadICPResults(rootPath: String, limit: Int = 0): List<TransformMatrix> {
    val root = File(rootPath)
    val icpResultsDir = root.resolve("actor_transforms")
    val transformFiles = icpResultsDir.walk().filter { it.isFile }
    val files = if (limit > 0) transformFiles.take(limit).toList() else transformFiles.toList()
    return files.map { file ->
        // Use frame id as id
        val (first, second) = file.nameWithoutExtension.split(" ")
        val lines = file.readLines().map { it.trim() }
        val innerMat = listOf(lines[1].split(" "), lines[2].split(" "), lines[3].split(" "))
        TransformMatrix(
            Pair(first, second),
            RotationMatrix.fromStringList(innerMat),
            TranslationMatrix.fromStringList(innerMat),
            lines[0].toDouble()
        )
    }
}