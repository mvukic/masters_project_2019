import charts.anglesDifferencesCharts
import charts.groundTruthCharts
import charts.icpCharts
import charts.locationDifferencesCharts
import load.loadGroundTruth
import load.loadICPResults
import models.shared.Euler
import models.shared.Point
import models.icp.toEuler
import stats.calculateEulerAngles
import stats.calculatePoints
import stats.calculatePointsExperimental
import stats.distanceAndTime

fun main(args: Array<String>)  {
    // Read data from files
    val groundTruth = loadGroundTruth(args[0])
    val icpTransformations = loadICPResults(args[0])

    // Ground truth locations, rotations and timestamps
    val locations: List<Point> = groundTruth.locations()
    val angles: List<Euler> = groundTruth.rotations()

    // Calculate points and angles by combining ground truth and icp transformation matrix
    val calculatedLocations: List<Point> = calculatePoints(icpTransformations, locations)
    val calculatedAngles: List<Euler> = calculateEulerAngles(icpTransformations.toEuler(), angles)

    // Prints distance and time traveled
    distanceAndTime(locations, calculatedLocations, groundTruth.timestamps())

    // Show basic charts
    groundTruthCharts(groundTruth)
//    icpCharts(groundTruth.timestamps(), icpTransformations)

    // Show chart indicating location differences
//    locationDifferencesCharts(groundTruth.timestamps(), locations, calculatedLocations)

//    anglesDifferencesCharts(groundTruth.timestamps(), angles, calculatedAngles)
}



