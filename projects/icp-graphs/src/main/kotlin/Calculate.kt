import models.basic.EulerAngles
import models.basic.Point
import models.basic.TransformMatrix
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Calculates points starting from first real point
 */
fun calculatePoints(icp: List<TransformMatrix>, realPoints:  List<Point>): List<Point> {
    // List of calculated points
    val calculatedPoints = mutableListOf(realPoints.first())
    // or each real point
    realPoints.forEachIndexed { index, _ ->
        // Skip first real point
        if (index == 0) return@forEachIndexed
        // Multiply rotation matrix with previous real point
        val nrp = icp[index - 1].rotation * realPoints[index - 1]
        // Save point
        calculatedPoints.add(nrp)
    }
    return calculatedPoints
}

/**
 * Calculates euler angles starting from first real angles
 */
fun calculateEulerAngles(icp: List<EulerAngles>, realAngles:  List<EulerAngles>): List<EulerAngles> {
    val calculatedAngles = mutableListOf(realAngles.first())
    realAngles.forEachIndexed { index, _ ->
        if (index == 0) return@forEachIndexed
        val nea = EulerAngles(
            roll = realAngles[index - 1].roll.toRad() + icp[index - 1].roll,
            pitch = realAngles[index - 1].pitch.toRad() + icp[index - 1].pitch,
            yaw = realAngles[index - 1].yaw.toRad() + icp[index - 1].yaw
        )
        calculatedAngles.add(nea)
    }
    return calculatedAngles
}

fun calculateAnglesDifferences(angles: List<EulerAngles>): List<EulerAngles> {
    return angles.windowed(2).map {
        val first = it[0]
        val second = it[1]
        val deltaRoll = (first.roll - second.roll).absoluteValue
        val deltaPitch = (first.pitch - second.pitch).absoluteValue
        val deltaYaw = (first.yaw - second.yaw).absoluteValue
        EulerAngles(deltaRoll, deltaPitch, deltaYaw)
    }
}

/**
 * Calculate differences between points
 */
fun calculateCoordinateDifferences(points: List<Point>): List<Point> {
    return points.windowed(2).map {
        val first = it[0]
        val second = it[1]
        val deltax = (first.x - second.x).absoluteValue
        val deltay = (first.y - second.y).absoluteValue
        val deltaz = (first.z - second.z).absoluteValue
        Point(deltax, deltay, deltaz)
    }
}

/**
 * Calcualate distance traveled betweeen points
 */
fun calculateTravelDistance(points: List<Point>): Double {
    var traveled = 0.0
    points.windowed(2).forEach {
        val x1 = it[0].x
        val y1 = it[0].y
        val x2 = it[1].x
        val y2 = it[1].y
        traveled += sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
    }
    println("Real distance traveled: $traveled")
    return traveled
}