package charts

import models.icp.TransformMatrix
import models.icp.toEuler
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.sign

fun icpCharts(timestampsFull: List<Double>, icpTransformations: List<TransformMatrix>) {

    val timestamps = timestampsFull.drop(1)
//    quaternionCharts(icpTransformations, timestamps)

    // Real RPY
    val roll = icpTransformations.map { wrapRadians(it.angles.roll) }
    val pitch = icpTransformations.map { wrapRadians(it.angles.pitch) }
    val yaw = icpTransformations.map { wrapRadians(it.angles.yaw) }

//    rpyCharts(timestamps, roll, pitch, yaw)

    // RPY from rotation matrix
    val rollCalc = icpTransformations.toEuler().map { wrapRadians(it.roll) }
    val pitchCalc = icpTransformations.toEuler().map { wrapRadians(it.pitch) }
    val yawCalc = icpTransformations.toEuler().map { wrapRadians(it.yaw) }
//    compareRPY(timestamps, Pair(roll, rollCalc), Pair(pitch, pitchCalc), Pair(yaw, yawCalc))
}

fun quaternionCharts(transformations: List<TransformMatrix>, timestamps: List<Double>) {
    val quaternion = transformations.map { it.angles.toQuaternion() }
    val xQuat = quaternion.map { it.x }
    val yQuat = quaternion.map { it.y }
    val zQuat = quaternion.map { it.z }
    val wQuat = quaternion.map { it.w }
        val quaternionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "ICP quaternions"
        xAxisTitle("timestamps [s]")
        yAxisTitle("rotations")
    }.build()
    quaternionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    quaternionChart.addSeries("X", timestamps, xQuat)
    quaternionChart.addSeries("Y", timestamps, yQuat)
//    quaternionChart.addSeries("Z", timestamps, zQuat)
//    quaternionChart.addSeries("W", timestamps, wQuat)
    SwingWrapper(quaternionChart).displayChart()
}

fun compareRPY(timestamps: List<Double>, roll: Pair<List<Double>, List<Double>>, pitch:  Pair<List<Double>, List<Double>>, yaw:  Pair<List<Double>, List<Double>>) {
    val icpRollConversionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "ICP roll comparison (conversion)"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Roll")
    }.build()
    icpRollConversionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpRollConversionChart.addSeries("Roll", timestamps, roll.first)
    icpRollConversionChart.addSeries("Roll from rotation", timestamps, roll.second)
    SwingWrapper(icpRollConversionChart).displayChart()

    val icpPitchConversionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "ICP pitch comparison (conversion)"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Pitch")
    }.build()
    icpPitchConversionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpPitchConversionChart.addSeries("Pitch", timestamps, pitch.first)
    icpPitchConversionChart.addSeries("Pitch from rotation", timestamps, pitch.second)
    SwingWrapper(icpPitchConversionChart).displayChart()

    val icpYawConversionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "ICP yaw comparison (conversion)"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Yaw")
    }.build()
    icpYawConversionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpYawConversionChart.addSeries("Yaw", timestamps, yaw.first)
    icpYawConversionChart.addSeries("Yaw from rotation", timestamps, yaw.second)
    SwingWrapper(icpYawConversionChart).displayChart()
}

fun rpyCharts(timestamps: List<Double>, roll: List<Double>, pitch: List<Double>, yaw: List<Double>) {
    val rollChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotation roll"
        xAxisTitle("timestamps [s]")
        yAxisTitle("roll")
    }.build()
    rollChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rollChart.addSeries("roll", timestamps, roll)
    SwingWrapper(rollChart).displayChart()

    val pitchChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotation pitch"
        xAxisTitle("timestamps [s]")
        yAxisTitle("pitch")
    }.build()
    pitchChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    pitchChart.addSeries("Pitch", timestamps, pitch)
    SwingWrapper(pitchChart).displayChart()

    val yawChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotation yaw"
        xAxisTitle("timestamps [s]")
        yAxisTitle("yaw")
    }.build()
    yawChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    yawChart.addSeries("Yaw", timestamps, yaw)
    SwingWrapper(yawChart).displayChart()

    val rotationsChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotations"
        xAxisTitle("timestamps [s]")
        yAxisTitle("rotation")
    }.build()
    rotationsChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsChart.addSeries("Yaw", timestamps, yaw)
    rotationsChart.addSeries("Pitch", timestamps, pitch)
    rotationsChart.addSeries("Roll", timestamps, roll)
    SwingWrapper(rotationsChart).displayChart()
}

fun wrapRadians(value: Double): Double {
    val limit = 3.1
    val abs = value.absoluteValue
    val sign = value.sign
    return if(value > limit) (PI - abs)*sign else abs*sign
}