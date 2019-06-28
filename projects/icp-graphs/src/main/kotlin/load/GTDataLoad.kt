package load

import models.gt.GroundTruth
import models.gt.GroundTruthFrame
import models.gt.Transform
import java.io.File

fun loadGroundTruth(rootPath: String): GroundTruth {
    val root = File(rootPath)
    val transformsDir = root.resolve("actor_transforms")
    val relativeTransformFile = root.resolve("relative_transform.txt")

    val files = transformsDir.walk().filter { it.isFile }.toList()
    val mapped = files.map { file ->
        // Use frame id as id
        val frameId = file.nameWithoutExtension
        // Parse ground truth transform
        val (timestamp, transform) = file.readLines().map { it.trim() }
        GroundTruthFrame(frameId, timestamp.toDouble(), Transform.fromString(transform))
    }
    // Parse transform between lidar and vehicle
    val relativeTransform = Transform.fromString(relativeTransformFile.readText().trim())
    return GroundTruth(
        frames = mapped.sortedBy { it.frameId },
        lidarToVehicleTransform = relativeTransform
    )
}

