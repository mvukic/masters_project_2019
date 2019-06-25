package stats

import models.shared.Euler
import models.shared.Point
import models.icp.TransformMatrix
import toRad
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

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
        val nrp = icp[index - 1].rotation * realPoints[index - 1]
        // Save point
        calculatedPoints.add(nrp)
    }
    return calculatedPoints
}

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

fun calculateAnglesDifferences(angles: List<Euler>): List<Euler> {
    return angles.windowed(2).map {
        val first = it[0]
        val second = it[1]
        val deltaRoll = (first.roll - second.roll).absoluteValue
        val deltaPitch = (first.pitch - second.pitch).absoluteValue
        val deltaYaw = (first.yaw - second.yaw).absoluteValue
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