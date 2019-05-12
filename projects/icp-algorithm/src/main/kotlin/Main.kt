import models.Data
import models.Frame
import org.knowm.xchart.QuickChart
import java.io.File

fun main(args: Array<String>)  {
    val data = setupData(args[0])
}

 fun setupData(rootPath: String): Data {
     val root = File(rootPath)
     val transformsDir = root.resolve("actor_transforms")
     val pointCloudsDir = root.resolve("point_clouds")
     val relativeTransformFile = root.resolve("relative_transform.txt")

     val transformFiles = transformsDir.walk().filter {it.isFile}
     val pointCloudFiles = pointCloudsDir.walk().filter {it.isFile}
     val zipped = transformFiles.zip(pointCloudFiles).toList()
     val mapped = zipped.map { pair ->
        val frameId = pair.first.nameWithoutExtension
        val transform = pair.first.readText().trim().toTransform()
        val points = pair.second.readLines().drop(7).map { it.trim().toPoint() }
        Frame(frameId, transform, points)
     }
     val relativeTransform = relativeTransformFile.readText().trim().toTransform()
     return Data(
         frames = mapped.sortedBy { it.frameId },
         lidarToVehicleTransform = relativeTransform
     )
}

