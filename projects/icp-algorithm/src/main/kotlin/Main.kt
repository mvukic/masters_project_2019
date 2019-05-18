import models.Frame
import models.basic.Point
import samples.SampleData

fun main(args: Array<String>)  {
    val data = setupData(args[0], onlyTransforms = false, limit = 2)
//    val data = SampleData()
//    showGraphs(data)

    data.frames.windowed(2).forEach {frames ->
        val (first, second) = frames
        val firstCentroid = findCentroid(first)
        val secondCentroid = findCentroid(second)
        println("${first.points.size} $firstCentroid")
        println("${second.points.size} $secondCentroid")
    }
}

fun findCentroid(frame: Frame): Point {
    val n = frame.points.size
    val centroidSum = frame.points.reduce { acc: Point, point: Point -> Point(acc.x + point.x, acc.y + point.y, acc.z + point.z) }
    return Point(centroidSum.x / n, centroidSum.y / n, centroidSum.z / n)
}



