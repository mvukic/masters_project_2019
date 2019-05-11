import java.io.File

fun main(args: Array<String>) {
    val root = File(args[0])
    val transformsDir = root.resolve("actor_transforms")
    val pointCloudsDir = root.resolve("point_clouds")
    val relativeTransformFile = root.resolve("relative_transform.txt")

    pointCloudsDir.walk().sortedBy { it.nameWithoutExtension }.forEach {
        println("Scan: ${it.name}")
    }

    transformsDir.walk().sortedBy { it.nameWithoutExtension }.forEach {
        println("Transform: ${it.name}")
    }

    println(relativeTransformFile.readText().trim().toTransform())


}