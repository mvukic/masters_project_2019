import models.Data
import models.Frame
import java.io.File

fun setupData(rootPath: String, onlyTransforms: Boolean = false, limit: Int = 0): Data {
    val root = File(rootPath)
    val transformsDir = root.resolve("actor_transforms")
    val pointCloudsDir = root.resolve("point_clouds")
    val relativeTransformFile = root.resolve("relative_transform.txt")

    val transformFiles = transformsDir.walk().filter {it.isFile}
    val pointCloudFiles = pointCloudsDir.walk().filter {it.isFile}
    val zipped = if (limit > 0) transformFiles.zip(pointCloudFiles).take(limit).toList() else transformFiles.zip(pointCloudFiles).toList()
    val mapped = zipped.map { pair ->
        // Use frame id as id
        val frameId = pair.first.nameWithoutExtension
        // Parse ground truth transform
        val (timestamp, transform) = pair.first.readLines().map { it.trim() }
        // PArse point cloud
        val points = if (onlyTransforms) listOf() else pair.second.readLines().drop(7).map { it.trim().toPoint() }
        Frame(frameId, timestamp.toDouble(), transform.toTransform(), points)
    }
    // Parse transform between lidar and vehicle
    val relativeTransform = relativeTransformFile.readText().trim().toTransform()
    return Data(
        frames = mapped.sortedBy { it.frameId },
        lidarToVehicleTransform = relativeTransform
    )
}