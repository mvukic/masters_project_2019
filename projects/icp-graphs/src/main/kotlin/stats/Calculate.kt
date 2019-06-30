package stats

import models.shared.Euler
import models.shared.Point
import models.icp.TransformMatrix
import models.shared.timesPoint
import kotlin.math.*

/**
 * Calculates points starting from first real point
 */
fun calculatePoints(icp: List<TransformMatrix>, realPoints:  List<Point>): List<Point> {
    // List of calculated points
    val calculatedPoints = mutableListOf(realPoints.first())
    // For each real point
    realPoints.forEachIndexed { index, _ ->
        // Skip first real point
        if (index == 0) return@forEachIndexed
        // Multiply rotation matrix with previous real point
        // Gives us next point according to transformation matrix
        val nrp = icp[index - 1] * realPoints[index - 1]
        // Save point
        calculatedPoints.add(nrp)
    }
    return calculatedPoints
}

/**
 * Calculate other points just by using first real point and transformation matrix's
 */
//fun calculatePointsExperimental(icp: List<TransformMatrix>, realPoints:  List<Point>): List<Point> {
//    val calculatedPoints = mutableListOf(realPoints.first())
//    val point = realPoints.first()
//    var transformMatrix = icp.first().matrix
//    // for each transformation
//    icp.drop(1).forEach { transform ->
//        // multiply point and current transformation matrix
//        calculatedPoints.add(transformMatrix.timesPoint(point))
//        // multiply previous transformation matrix and current transformation matrix
//        transformMatrix = transform * transformMatrix
//    }
//    return calculatedPoints
//}

/**
 * Calculates euler angles starting from first real angles
 */
fun calculateEulerAngles(icp: List<Euler>, realAngles:  List<Euler>): List<Euler> {
    val calculatedAngles = mutableListOf(realAngles.first())
    realAngles.forEachIndexed { index, _ ->
        if (index == 0) return@forEachIndexed
        val nea = Euler(
            roll = realAngles[index - 1].roll + icp[index - 1].roll,
            pitch = realAngles[index - 1].pitch + icp[index - 1].pitch,
            yaw = realAngles[index - 1].yaw + icp[index - 1].yaw
        )
        calculatedAngles.add(nea)
    }
    return calculatedAngles
}

class Angle(d: Double) {
    val value = when {
        d in -PI .. PI -> d
        d > PI -> (d - PI) % (2*PI) - PI
        else -> (d + PI) % (2*PI) + PI
    }

    operator fun minus(other: Angle) = Angle(this.value - other.value)
    operator fun plus(other: Angle) = Angle(this.value + other.value)
}

fun calculateAnglesDifferences(angles: List<Euler>): List<Euler> {
    return angles.windowed(2).map {
        val first = it[0]
        val second = it[1]
        val deltaRoll = (Angle(first.roll) - Angle(second.roll)).value
        val deltaPitch = (Angle(first.pitch) - Angle(second.pitch)).value
        val deltaYaw = (Angle(first.yaw) - Angle(second.yaw)).value
        Euler(deltaRoll, deltaPitch, deltaYaw)
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
 * Calculate distance traveled between points
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
    return traveled
}

/**
 * Print distance traveled and time traveled
 */
fun distanceAndTime(locations: List<Point>, calculated: List<Point>, timestamps: List<Double>) {
    println("Real distance traveled: ${calculateTravelDistance(locations)} m")
    println("Distance with calculated: ${calculateTravelDistance(calculated)} m")
    println("Travel duration: ${timestamps.last() - timestamps.first()} s")
}

fun wrapRadians(value: Double): Double {
    val limit = 3.1
    val abs = value.absoluteValue
    val sign = value.sign
    return if(value > limit) (PI - abs)*sign else abs*sign
}

fun calculatePointsDifference(locations: List<Point>, calculated: List<Point>): List<Point> {
    return locations.zip(calculated).map {
        Point(
            x = (it.first.x - it.second.x).absoluteValue,
            y = (it.first.y - it.second.y).absoluteValue,
            z = (it.first.z - it.second.z).absoluteValue
        )
    }
}

fun calculateEulerDifference(angles: List<Euler>, calculated: List<Euler>): List<Euler> {
    return angles.zip(calculated).map {
        val yaw = (it.first.yaw - it.second.yaw).absoluteValue % PI
        Euler(
            roll = (it.first.roll - it.second.roll).absoluteValue,
            pitch = (it.first.pitch - it.second.pitch).absoluteValue,
            yaw = if (yaw > 3.1) yaw - PI else yaw
        )
    }
}
