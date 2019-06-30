package charts

import models.shared.Euler
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import stats.calculateEulerDifference
import stats.wrapRadians
import kotlin.math.absoluteValue

fun anglesDifferencesCharts(timestampsFull: List<Double>, angles: List<Euler>, calculated: List<Euler>) {
    val timestamps = timestampsFull.drop(1)

    val diffs = calculateEulerDifference(angles, calculated)
    diffBetweenRealAndCalc(timestampsFull, diffs)

    calculatedAngles(timestampsFull, calculated)
//    calculatedAnglesQuaternion(timestampsFull, calculated)


    // Calculate points and angles using real data and icp rotation matrix
    val realAnglesDiff = stats.calculateAnglesDifferences(angles)
    val icpAnglesDiff = stats.calculateAnglesDifferences(calculated)

    // Real euler angles differences
    val realRollDelta = realAnglesDiff.map { it.roll }
    val realPitchDelta = realAnglesDiff.map { it.pitch }
    val realYawDelta = realAnglesDiff.map { it.yaw }

//    realAngleDeltasChart(timestamps, realRollDelta, realPitchDelta, realYawDelta)

    // Calculated euler angles differences
    val icpRollDelta = icpAnglesDiff.map { it.roll }
    val icpPitchDelta = icpAnglesDiff.map { it.pitch }
    val icpYawDelta = icpAnglesDiff.map { it.yaw }

//    calculatedAngleDeltasChart(timestamps, icpRollDelta, icpPitchDelta, icpYawDelta)
}

fun overlayRotations(timestamps: List<Double>, angles: List<Euler>, calculated: List<Euler>) {

}

fun diffBetweenRealAndCalc(timestamps: List<Double>, diffs: List<Euler>) {
    val roll = diffs.map { it.roll }
    val pitch = diffs.map { it.pitch }
    val yaw = diffs.map { it.yaw }
    val rotationsEulerChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Razlike referentnih i estimiranih rotacija"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("Δ rotacije [rad]")
    }.build()
    rotationsEulerChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsEulerChart.addSeries("ΔValjanje", timestamps, roll)
    rotationsEulerChart.addSeries("ΔPoniranje", timestamps, pitch)
    rotationsEulerChart.addSeries("ΔSkretanje", timestamps, yaw)
    SwingWrapper(rotationsEulerChart).displayChart()
}

fun calculatedAngles(timestamps: List<Double>, calculated: List<Euler>) {
    val roll = calculated.map { it.roll }
    val pitch = calculated.map { it.pitch }
    val yaw = calculated.map { it.yaw.absoluteValue }
    val rotationsEulerChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Estimirane rotacije"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("rotacija [rad]")
    }.build()
    rotationsEulerChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsEulerChart.addSeries("Valjanje", timestamps, roll)
    rotationsEulerChart.addSeries("Poniranje", timestamps, pitch)
    rotationsEulerChart.addSeries("Skretanje", timestamps, yaw)
    SwingWrapper(rotationsEulerChart).displayChart()
}

fun calculatedAnglesQuaternion(timestamps: List<Double>, angles: List<Euler>) {
    val quaternion = angles.map { it.toQuaternion() }
    val xQuat = quaternion.map { it.x }
    val yQuat = quaternion.map { it.y }
    val zQuat = quaternion.map { it.z }
    val wQuat = quaternion.map { it.w }
    val rotationsQuaternionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Estimirana rotacija prikazana pomoću kvaterniona"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("rotacija [rad]")
    }.build()
    rotationsQuaternionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsQuaternionChart.addSeries("X", timestamps, xQuat)
    rotationsQuaternionChart.addSeries("Y", timestamps, yQuat)
//    rotationsQuaternionChart.addSeries("Z", timestamps, zQuat)
//    rotationsQuaternionChart.addSeries("W", timestamps, wQuat)
    SwingWrapper(rotationsQuaternionChart).displayChart()
}

fun calculatedAngleDeltasChart(timestamps: List<Double>, roll: List<Double>, pitch: List<Double>, yaw: List<Double>) {
    val realAngleDeltasChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Differences between two consecutive points in angles (calculated)"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Euler angles")
    }.build()
    realAngleDeltasChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    realAngleDeltasChart.addSeries("Delta roll", timestamps, roll)
    realAngleDeltasChart.addSeries("Delta pitch", timestamps, pitch)
    realAngleDeltasChart.addSeries("Delta yaw", timestamps, yaw)
    SwingWrapper(realAngleDeltasChart).displayChart()
}

fun realAngleDeltasChart(timestamps: List<Double>, roll: List<Double>, pitch: List<Double>, yaw: List<Double>) {
    val realAngleDeltasChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Differences between two consecutive points in angles"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Euler angles")
    }.build()
    realAngleDeltasChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    realAngleDeltasChart.addSeries("Delta roll", timestamps, roll)
    realAngleDeltasChart.addSeries("Delta pitch", timestamps, pitch)
    realAngleDeltasChart.addSeries("Delta yaw", timestamps, yaw)
    SwingWrapper(realAngleDeltasChart).displayChart()
}