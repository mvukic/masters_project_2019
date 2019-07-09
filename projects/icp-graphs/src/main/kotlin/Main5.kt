import charts.anglesDifferencesCharts
import charts.groundTruthCharts
import charts.icpCharts
import charts.locationDifferencesCharts
import load.loadGroundTruth
import load.loadICPResults
import models.shared.Euler
import models.shared.Point
import models.icp.toEuler
import stats.*
import kotlin.math.absoluteValue

fun main(args: Array<String>)  {
    // Read data from files
    val groundTruth = loadGroundTruth(args[0])
    val icpTransformations = loadICPResults(args[0])

    // Ground truth locations, rotations
    val locations: List<Point> = groundTruth.locations()
    println("Diff x ${locations[1].x - locations[0].x}")
    println("Diff y ${locations[1].y - locations[0].y}")
    println("Diff z ${locations[1].z - locations[0].z}")
    println("Locaions size: ${locations.size}")
//    val angles: List<Euler> = groundTruth.rotations()

    // Calculate points and angles by combining ground truth and icp transformation matrix
    val calculatedTrans = calculatePointsExperimental(icpTransformations,groundTruth.frames.first().transform)
    val d1 = locations.last().x - locations.first().x
    var calculatedLocations: List<Point> = calculatedTrans.map { mat -> Point(mat[0][3], mat[1][3], mat[2][3]) }
    println("CAlc size: ${calculatedLocations.size}")
    println("First calc ${calculatedLocations.first()}")
    println("Last calc ${calculatedLocations.last()}")
    val d2 = calculatedLocations.last().x - calculatedLocations.first().x
    val k = (d1 / d2).absoluteValue
    println("$d1 $d2 $k")
    val dists = mutableListOf<Double>()
    calculatedLocations.windowed(2) {
        val d_old = it[1].x - it[0].x
        dists.add(d_old * k)
    }
    println("Dists size: ${dists.size}")
    var pts = mutableListOf(calculatedLocations.first())
    (0 until locations.size).forEach {
        if (it == 0) return@forEach
        val last = pts.last()
        val np = Point(last.x + dists[it - 1], calculatedLocations[it - 1].y, calculatedLocations[it - 1].z)
        pts.add(np)
    }
    pts = pts.map {
        Point(it.x + 100.0, it.y + 0.1, it.z)
    }.toMutableList()
    locationDifferencesCharts(groundTruth.timestamps(), locations, pts)
//    val calculatedLocations: List<Point> = calculatePoints(icpTransformations, locations)
//    val calculatedAngles: List<Euler> = calculateEulerAngles(icpTransformations.toEuler(), angles)

    // Prints distance and time traveled
//    distanceAndTime(locations, calculatedLocations, groundTruth.timestamps())

//    MEACoordinates(locations, calculatedLocations)
//    MEAAngles(angles, calculatedAngles)
//    MSECoordinates(locations, calculatedLocations)
//    MSEAngles(angles, calculatedAngles)

    // Show basic charts
//    groundTruthCharts(groundTruth)
//    icpCharts(groundTruth.timestamps(), icpTransformations)

    // Show chart indicating location differences

//    anglesDifferencesCharts(groundTruth.timestamps(), angles, calculatedAngles)
}
