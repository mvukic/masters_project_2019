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
    val allGt = loadGroundTruth(args[0])
    val groundTruth = allGt.copy(frames = allGt.frames)
    val icpTransformations = loadICPResults(args[0])

    // Ground truth locations, rotations
    val locations: List<Point> = groundTruth.locations()
    val angles: List<Euler> = groundTruth.rotations()

    // Calculate points and angles by combining ground truth and icp transformation matrix
    val calculatedTrans = calculatePointsExperimental(icpTransformations, groundTruth.frames.first().transform)
    val calculatedLocations: List<Point> = calculatedTrans.map { mat -> Point(mat[0][3], mat[1][3], mat[2][3]) }

//    val calculatedAngles: List<Euler> = calculateEulerAngles(icpTransformations.toEuler(), angles)
    val calculatedAngles = calcualteAnglesExperimental(calculatedTrans)

    // Prints distance and time traveled
    distanceAndTime(locations, calculatedLocations, groundTruth.timestamps())

    MEACoordinates(locations, calculatedLocations)
    MEAAngles(angles, calculatedAngles)
    MSECoordinates(locations, calculatedLocations)
    MSEAngles(angles, calculatedAngles)
    locationDifferencesCharts(groundTruth.timestamps(), locations, calculatedLocations)
    // Show basic charts
//    groundTruthCharts(groundTruth)
//    icpCharts(groundTruth.timestamps(), icpTransformations)

    // Show chart indicating location differences

    anglesDifferencesCharts(groundTruth.timestamps(), angles, calculatedAngles)
}
